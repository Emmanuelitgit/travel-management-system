package user_service.models;

import jakarta.persistence.*;
import lombok.*;
import user_service.config.AuditorData;

import java.time.ZonedDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_role_tb")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID roleId;
}
