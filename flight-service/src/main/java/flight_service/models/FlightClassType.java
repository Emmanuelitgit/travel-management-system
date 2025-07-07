package flight_service.models;

import flight_service.config.AuditorData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flight_class_type_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightClassType extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "flight class name cannot be null")
    private String name;
    private String description;
    private boolean isActive;
}
