package notification_service.serviceImpl;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import notification_service.dto.BookingPayload;
import notification_service.dto.PaymentAuthorizationPayload;
import notification_service.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j

public class BookingServiceImpl {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Autowired
    public BookingServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @KafkaListener(topics = "bookingNotification", containerFactory = "bookingKafkaListenerContainerFactory", groupId = "booking-group")
    public void SendToCustomer(BookingPayload booking){
        try {
            log.info("About to send booking notification to user:->>>{}", booking.getEmail());

            // setting email items
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Trip Summary");
            helper.setFrom("eyidana001@gmail.com");
            helper.setTo(booking.getEmail());

            // setting variables values to passed to the template
            Context context = new Context();
            context.setVariable("booking", booking);

            String htmlContent = templateEngine.process("BookingTemplate", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Booking notification sent:->>>>>>>>");

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            throw new ServerException("Internal server error!");
        }
    }

    @KafkaListener(topics = "paymentNotification", containerFactory = "paymentKafkaListenerContainerFactory", groupId = "payment-group")
    public void SendPaymentAuthorization(PaymentAuthorizationPayload payment){
        try {
            log.info("About to send payment authorization url to user:->>>>{}", payment.getEmail());

            // setting email items
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Payment Authorization");
            helper.setFrom("eyidana001@gmail.com");
            helper.setTo(payment.getEmail());

            // setting variables values to passed to the template
            Context context = new Context();
            context.setVariable("paymentUrl", payment.getAuthorization_url());
            context.setVariable("fullName", payment.getEmail());

            String htmlContent = templateEngine.process("PaymentTemplate", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Payment authorization code sent:->>>>>>>>");

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            throw new ServerException("Internal server error!");
        }
    }
}