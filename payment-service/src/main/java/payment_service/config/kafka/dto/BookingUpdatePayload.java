package payment_service.config.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdatePayload {
    private UUID id;
    private String paymentStatus;
    private String bookingStatus;
}
