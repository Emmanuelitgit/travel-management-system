package flight_service.service;

import flight_service.dto.ResponseDTO;
import flight_service.models.Airport;
import flight_service.models.FlightAirlineType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AirportService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveAirport(Airport airport);
    ResponseEntity<ResponseDTO> updateAirport(UUID airportId, Airport airport);
    ResponseEntity<ResponseDTO> removeAirport(UUID airportId);
    ResponseEntity<ResponseDTO> getAirportById(UUID airportId);
}
