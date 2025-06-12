package user_service.config.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPPayload {
    private UUID userId;
    @NotNull(message = "email cannot be null")
    private String email;
    @NotNull(message = "otp code cannot be null")
    private int otpCode;
    private String firstName;
    private String lastName;
}
