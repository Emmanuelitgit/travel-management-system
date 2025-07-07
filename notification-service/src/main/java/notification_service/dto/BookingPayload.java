package notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPayload {
    private String firstName;
    private String lastName;
    private String message;
    private String email;
    private String departure;
    private String destination;
    private String departureDate;
    private String arrivalDate;
    private String airline;
    private String classType;
    private String tripType;
    private Integer seatNumber;
    private Float price;
    private Integer numberOfSeats;
    private Float totalPrice;
}
