package flight_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "flight_seat_type_tb")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "flight seat name cannot be null")
    private String name;
    private String description;
    private boolean isActive;
}
