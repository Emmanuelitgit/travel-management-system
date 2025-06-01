package flight_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "flight_class_type_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightClassType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "flight class name cannot be null")
    private String name;
    private String description;
    private boolean isActive;
}
