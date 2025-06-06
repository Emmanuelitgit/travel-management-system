package booking_service.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String username;
    private String role;
}