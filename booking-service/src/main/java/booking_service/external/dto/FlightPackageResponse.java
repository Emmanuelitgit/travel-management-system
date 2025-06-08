package booking_service.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightPackageResponse {
    private UUID id;
    private String destination;
    private String departure;
    private Integer availableSeats;
    private Float price;
    private String departureDate;
    private String arrivalDate;
    private boolean nonStop;
    private String seatType;
    private String tripType;
    private String classType;
    private String airline;
    private String description;
}