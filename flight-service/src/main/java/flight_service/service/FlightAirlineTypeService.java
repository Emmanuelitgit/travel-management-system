package flight_service.service;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightAirlineType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface FlightAirlineTypeService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveAirline(FlightAirlineType flightAirlineType);
    ResponseEntity<ResponseDTO> updateAirline(UUID airlineId, FlightAirlineType flightAirlineType);
    ResponseEntity<ResponseDTO> removeAirline(UUID airlineId);
    ResponseEntity<ResponseDTO> getAirlineById(UUID airlineId);
}
