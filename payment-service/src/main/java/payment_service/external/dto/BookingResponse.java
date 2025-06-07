package payment_service.external.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class BookingResponse {
    private UUID bookingId;
    private Integer seatNumber;
    private Float totalPrice;
    private String paymentStatus;
    private String bookingStatus;
    Map<String, Object> user;
    Map<String, Object> flight;
}
