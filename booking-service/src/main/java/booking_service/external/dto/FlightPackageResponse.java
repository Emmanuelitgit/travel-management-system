package booking_service.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightPackageResponse {
    private String id;
    private String destination;
    private String departure;
    private Integer availableSeats;
    private Double price;
    private String departureDate;
    private String arrivalDate;
    private boolean nonStop;
    private String seatType;
    private String tripType;
    private String classType;
    private String airlineId;
    private String description;

    private String createdBy;
    private String createdAt;
    private String lastModifiedBy;
    private String lastModifiedAt;
}