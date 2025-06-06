package booking_service.dto;

import booking_service.external.dto.FlightPackageResponse;
import booking_service.external.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private UUID bookingId;
    private Integer seatNumber;
    private Float totalPrice;
    private String paymentStatus;
    private String bookingStatus;

    private UserResponse user;
    private FlightPackageResponse flight;
}