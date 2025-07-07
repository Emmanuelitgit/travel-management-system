package flight_service.repo;

import flight_service.models.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AirportRepo extends JpaRepository<Airport, UUID> {
}
