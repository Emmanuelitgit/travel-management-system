package flight_service.models;

import flight_service.config.AuditorData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "airport_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Airport extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "airport name cannot be null")
    private String name;
    @NotNull(message = "airport country cannot be null")
    private String country;
    @NotNull(message = "airport city cannot be null")
    private String city;
    private String description;
}
