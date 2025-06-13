package flight_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class Config {

    private final JwtAuthConverter jwtAuthConverter;

    @Autowired
    public Config(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests((auth)->{
                    auth
                            .anyRequest().authenticated();
                })
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer((auth->{
                    auth.jwt(jwt->{
                        jwt.jwtAuthenticationConverter(jwtAuthConverter);
                    });
                }))
                .build();
    }

    @Bean
    AuditorAware auditorAware(){
        return new AuditorAwareImpl();
    }
}
