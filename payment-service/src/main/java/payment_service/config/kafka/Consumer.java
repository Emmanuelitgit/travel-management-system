package payment_service.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import payment_service.config.kafka.dto.PaymentUpdatePayload;

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


    @Bean
    public ConsumerFactory<String, PaymentUpdatePayload> paymentUpdateConsumerFactory() {
        Map<String, Object> props = commonConfigs();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(PaymentUpdatePayload.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentUpdatePayload> paymentUpdateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentUpdatePayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentUpdateConsumerFactory());
        return factory;
    }

    // Shared configuration for all consumers
    private Map<String, Object> commonConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        return props;
    }
}
