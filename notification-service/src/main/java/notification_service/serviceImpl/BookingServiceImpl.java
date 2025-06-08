package notification_service.serviceImpl;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import notification_service.dto.BookingPayload;
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
            log.info("In booking notification method:->>>>>>");

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
}
