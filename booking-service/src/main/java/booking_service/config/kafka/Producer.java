package booking_service.config.kafka;

import booking_service.config.kafka.dto.BookingNotificationPayload;
import booking_service.config.kafka.dto.BookingUpdatePayload;
import booking_service.config.kafka.dto.FlightUpdatePayload;
import booking_service.config.kafka.dto.PaymentUpdatePayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Producer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    // flight package update producer
    @Bean
    public ProducerFactory<String, FlightUpdatePayload> flightPackageUpdateProducerFactory() {
        Map<String, Object> configProps = commonConfigs();
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, FlightUpdatePayload> flightPackageUpdatekafkaTemplate() {
        return new KafkaTemplate<>(flightPackageUpdateProducerFactory());
    }

    // payment update producer
    @Bean
    public ProducerFactory<String, PaymentUpdatePayload> paymentUpdateProducerFactory() {
        Map<String, Object> configProps = commonConfigs();
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, PaymentUpdatePayload> paymentUpdatekafkaTemplate() {
        return new KafkaTemplate<>(paymentUpdateProducerFactory());
    }

    // booking notification producer
    @Bean
    public ProducerFactory<String, BookingNotificationPayload> bookingNotificationProducerFactory() {
        Map<String, Object> configProps = commonConfigs();
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BookingNotificationPayload> bookingNotificationkafkaTemplate() {
        return new KafkaTemplate<>(bookingNotificationProducerFactory());
    }

    // Shared configuration for all consumers
    private Map<String, Object> commonConfigs() {
        Map<String, Object> props= new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }
}