package flight_service.repo;

import flight_service.models.FlightSeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightSeatTypeRepo extends JpaRepository<FlightSeatType, UUID> {
}
