package flight_service.repo;

import flight_service.models.FlightSeats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightSeatsRepo extends JpaRepository<FlightSeats, UUID> {
}
