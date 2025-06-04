package booking_service.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AuditorAwareImpl implements AuditorAware {
    @Override
    public Optional getCurrentAuditor() {
        UUID id = UUID.fromString("609527d7-0aab-49c0-a22f-fe035c8a7382");
        return Optional.of(id);
    }
}
