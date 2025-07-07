package payment_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String payStackUrl;
    private String payStackSecret;
    private String bookingServiceUrl;
    private String userServiceUrl;
}
