package flight_service.service;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightPackage;
import flight_service.models.FlightSeatType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface FlightPackageService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveFlightPackage(FlightPackage flightPackage);
    ResponseEntity<ResponseDTO> updateFlightPackage(UUID packageId, FlightPackage flightPackage);
    ResponseEntity<ResponseDTO> removeFlightPackage(UUID packageId);
    ResponseEntity<ResponseDTO> getFlightPackageById(UUID packageId);
}
