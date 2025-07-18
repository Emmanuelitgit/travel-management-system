package flight_service.models;

import flight_service.config.AuditorData;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flight_seats_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightSeats extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID flightId;
    private String seatNumber;
    private boolean isBooked;
}
