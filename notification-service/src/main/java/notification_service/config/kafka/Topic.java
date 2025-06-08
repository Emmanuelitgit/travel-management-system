package notification_service.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Topic {
    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("otpNotification")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("bookingNotification")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
