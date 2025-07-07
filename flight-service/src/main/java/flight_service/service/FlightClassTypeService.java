package flight_service.service;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightAirlineType;
import flight_service.models.FlightClassType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface FlightClassTypeService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveFlightClass(FlightClassType flightClassType);
    ResponseEntity<ResponseDTO> updateFlightClass(UUID classId, FlightClassType flightClassType);
    ResponseEntity<ResponseDTO> removeFlightClass(UUID classId);
    ResponseEntity<ResponseDTO> getFlightClassById(UUID classId);
}
