package booking_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "bookings_tb")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private UUID id;
    @NotNull(message = "package id cannot be null")
    private UUID packageId;
    @NotNull(message = "seat number cannot be null")
    private Integer seatNumber;
    private Integer numberOfSeats;
    private Float totalPrice;
    private String paymentStatus;
    private String bookingStatus;
    @NotNull(message = "user id cannot be null")
    private UUID userId;
}
