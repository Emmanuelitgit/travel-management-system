package booking_service.rest;

import booking_service.dto.ResponseDTO;
import booking_service.models.Booking;
import booking_service.serviceImpl.BookingServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingRest {

    private final BookingServiceImpl bookingService;

    @Autowired
    public BookingRest(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return bookingService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveAirport(@Valid @RequestBody Booking booking){
        return bookingService.saveBooking(booking);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<ResponseDTO> updateAirport(@PathVariable UUID bookingId, @RequestBody Booking booking){
        booking.setId(bookingId);
        return bookingService.updateBooking(booking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ResponseDTO> getAirportById(@PathVariable UUID bookingId){
        return bookingService.getBookingById(bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<ResponseDTO> removeAirport(@PathVariable UUID bookingId){
        return bookingService.removeBooking(bookingId);
    }
}
