package notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPayload {
    private Booking booking;
    private String firstName;
    private String lastName;
    private String message;
    private String email;

    @Data
    static class Booking{
        private String departure;
        private String destination;
        private String departureDate;
        private String arrivalDate;
        private String airline;
        private String classType;
        private String tripType;
        private Integer seatNumber;
        private Integer price;
        private Integer numberOfSeats;
        private Float totalPrice;
    }
}
