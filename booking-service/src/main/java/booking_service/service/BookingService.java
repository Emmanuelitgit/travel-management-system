package booking_service.service;

import booking_service.dto.ResponseDTO;
import booking_service.models.Booking;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface BookingService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveBooking(Booking booking);
    ResponseEntity<ResponseDTO> updateBooking(Booking booking);
    ResponseEntity<ResponseDTO> removeBooking(UUID bookingId);
    ResponseEntity<ResponseDTO> cancelBooking(UUID bookingId);
    ResponseEntity<ResponseDTO> getBookingById(UUID bookingId);
    ResponseEntity<ResponseDTO> getBookingBySeatNumber(UUID seatNumber);
}
