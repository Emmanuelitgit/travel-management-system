package flight_service.service;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightClassType;
import flight_service.models.FlightSeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface FlightSeatTypeService{
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveFlightSeat(FlightSeatType flightSeatType);
    ResponseEntity<ResponseDTO> updateFlightSeat(UUID seatId, FlightSeatType flightSeatType);
    ResponseEntity<ResponseDTO> removeFlightSeat(UUID seatId);
    ResponseEntity<ResponseDTO> getFlightSeatById(UUID seatId);
}
