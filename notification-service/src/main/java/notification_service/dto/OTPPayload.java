package notification_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
public class OTPPayload {
    private UUID userId;
    @NotNull(message = "email cannot be null")
    private String email;
    @NotNull(message = "otp code cannot be null")
    private int otpCode;
    private String firstName;
    private String lastName;
}
