package payment_service.models;

import jakarta.persistence.*;
import lombok.*;
import payment_service.config.AuditorData;

import java.time.ZonedDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_tb")
public class Payment extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID bookingId;
    private Float amount;
    private Long transactionId;
    private String paymentStatus;
    private String access_code;
    private String reference;
    private String currency;
    private String channel;
    private String paymentDate;
}
