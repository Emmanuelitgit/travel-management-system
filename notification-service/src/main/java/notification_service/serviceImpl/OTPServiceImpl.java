package notification_service.serviceImpl;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import notification_service.dto.OTPPayload;
import notification_service.dto.ResponseDTO;
import notification_service.exception.BadRequestException;
import notification_service.exception.ServerException;
import notification_service.models.OTP;
import notification_service.repo.OTPRepo;
import notification_service.service.OTPService;
import notification_service.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

@Slf4j
@Service
public class OTPServiceImpl implements OTPService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final OTPRepo otpRepo;
    private final KafkaTemplate<String, OTPPayload> kafkaTemplate;

    @Autowired
    public OTPServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, OTPRepo otpRepo, KafkaTemplate<String, OTPPayload> kafkaTemplate) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.otpRepo = otpRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void testKafka(){
        OTPPayload otpPayload = new OTPPayload();
        otpPayload.setEmail("eyidana001@gmail.com");
        otpPayload.setFirstName("Yidana");
        otpPayload.setLastName("Emmanuel");
        otpPayload.setOtpCode(2435);

        kafkaTemplate.send("otpNotification", otpPayload);
    }

    /**
     * @description This method is used to send an otp code to a user given the required payload.
     * @param otpPayload
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 10h May 2025
     */
    @KafkaListener(topics = "otpNotification", containerFactory = "kafkaListenerContainerFactory", groupId = "otp-group")
    @Override
    public void sendOtp(OTPPayload otpPayload) {
        try {
            log.info("In send otp method:->>>>>>");

            // check if user have an existing otp. delete it if exist before sending a new one.
            OTP otpExist = otpRepo.findByUserId(otpPayload.getUserId());
            if (otpExist != null){
               otpRepo.deleteById(otpExist.getId());
            }

            // setting email items
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("OTP Verification");
            helper.setFrom("eyidana001@gmail.com");
            helper.setTo(otpPayload.getEmail());

            // setting variables values to be passed to the template
            Context context = new Context();
            otpPayload.setOtpCode(generateOTP());
            context.setVariable("otp", otpPayload.getOtpCode());
            context.setVariable("fullName", otpPayload.getFirstName() + " " + otpPayload.getLastName());

            String htmlContent = templateEngine.process("OTPTemplate", context);
            helper.setText(htmlContent, true);

            // saving otp to db
            OTP otp = saveOTP(otpPayload);
            if (otp == null){
                throw new BadRequestException("fail to save otp record");
            }

            log.info("Otp sent:->>>>>>>>");
            // sending message
            mailSender.send(message);

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            throw new ServerException("Internal server error!");
        }
    }

    // a helper method for saving otp record to the db
    public OTP saveOTP(OTPPayload otpPayload){
        OTP otp = new OTP();
        otp.setOtpCode(otpPayload.getOtpCode());
        otp.setStatus(false);
        otp.setExpireAt(ZonedDateTime.now().plusMinutes(2));
        otp.setUserId(otpPayload.getUserId());
        return otpRepo.save(otp);
    }

    /**
     * @description This method is used to verify user otp.
     * @param otpPayload
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 10h May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> verifyOtp(OTPPayload otpPayload) {

        // check if otp exist
        OTP otpExist = otpRepo.findByUserId(otpPayload.getUserId());
        if (otpExist == null){
            ResponseDTO response = AppUtils.getResponseDto("OTP record not found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        // check if the otp does not expire
        if (!ZonedDateTime.now().isBefore(otpExist.getExpireAt())){
            ResponseDTO response = AppUtils.getResponseDto("OTP has expired", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // check if otp entered by user match the one in the db
        if (otpPayload.getOtpCode() != otpExist.getOtpCode()){
            ResponseDTO response = AppUtils.getResponseDto("OTP do not match", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // remove otp after verification
        otpExist.setStatus(true);
        otpRepo.deleteById(otpExist.getId());

        ResponseDTO response = AppUtils.getResponseDto("OTP verified", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // a helper method to generate otp codes
    public Integer generateOTP(){
        RandomGenerator generator = new Random();
        return generator.nextInt(2001, 9000);
    }

    // a helper method to check user verification status during login
    public boolean checkOTPStatusDuringLogin(OTPPayload otpPayload){
        OTP otpExist = otpRepo.findByUserId(otpPayload.getUserId());
        return otpExist == null;
    }
}
