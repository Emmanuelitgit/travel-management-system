package notification_service.config.kafka;

import notification_service.dto.BookingPayload;
import notification_service.dto.OTPPayload;
import notification_service.dto.PaymentAuthorizationPayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Consumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;


// OTP notification consumer config
    @Bean
    public ConsumerFactory<String, OTPPayload> otpConsumerFactory() {
        Map<String, Object> props = commonConfigs("otp-group");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(OTPPayload.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OTPPayload> otpKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OTPPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(otpConsumerFactory());
        return factory;
    }


    // booking notification config
    @Bean
    public ConsumerFactory<String, BookingPayload> bookingConsumerFactory() {
        Map<String, Object> props = commonConfigs("booking-group");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(BookingPayload.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingPayload> bookingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BookingPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingConsumerFactory());
        return factory;
    }


    // payment notification consumer config
    @Bean
    public ConsumerFactory<String, PaymentAuthorizationPayload> paymentConsumerFactory() {
        Map<String, Object> props = commonConfigs("payment-group");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(PaymentAuthorizationPayload.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentAuthorizationPayload> paymentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentAuthorizationPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        return factory;
    }


    // Shared configuration for all consumers
    private Map<String, Object> commonConfigs(String group) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        return props;
    }
}