package flight_service.models;

import flight_service.config.AuditorData;
import flight_service.dto.enums.TripType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "flight_package_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightPackage extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "destination cannot be null")
    private UUID destination;
    @NotNull(message = "departure cannot be null")
    private UUID departure;
    @NotNull(message = "available seats cannot be null")
    private Integer availableSeats;
    @NotNull(message = "price cannot be null")
    private Float price;
    private ZonedDateTime departureDate;
    private ZonedDateTime arrivalDate;
    @NotNull(message = "non stop value cannot be null")
    private boolean nonStop;
    @NotNull(message = "seat type cannot be null")
    private UUID seatType; // Aisle or Window
    @NotNull(message = "trip type cannot be null")
    private TripType tripType; // one-way or round-trip
    @NotNull(message = "class type cannot be null")
    private UUID classType;
    @NotNull(message = "airline type cannot be null")
    private UUID airlineId;
    private String description;
}