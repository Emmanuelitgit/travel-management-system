package flight_service.config.kafka;

import flight_service.config.kafka.dto.FlightUpdatePayload;
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
    public ConsumerFactory<String, FlightUpdatePayload> flightPackageConsumerFactory() {
        Map<String, Object> props = commonConfigs("flight-group");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(FlightUpdatePayload.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FlightUpdatePayload> flightPackageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FlightUpdatePayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(flightPackageConsumerFactory());
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