package flight_service.dto.projections;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface FlightPackageProjection {
    UUID getId();
    String getDestination(); // the destination airport name
    String getDeparture(); // the airport the departure takes place
    Integer getAvailableSeats();
    Float getPrice();
    LocalDateTime getDepartureDate();
    LocalDateTime getArrivalDate();
    boolean getNonStop();
    String getSeatType(); // Aisle or Window
    String getTripType(); // one-way or round-trip
    String getClassType();
    String  getAirline();
    String getDescription();
}
