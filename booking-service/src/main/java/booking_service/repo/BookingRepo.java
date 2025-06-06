package booking_service.repo;

import booking_service.dto.BookingProjection;
import booking_service.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepo extends JpaRepository<Booking, UUID> {

    @Query(value = "SELECT * FROM bookings_tb ", nativeQuery = true)
    List<BookingProjection> fetchAllBookings();

    Optional<Booking> findBySeatNumber(Integer seatNumber);
}
