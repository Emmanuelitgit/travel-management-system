package flight_service.serviceImpl;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightSeatType;
import flight_service.service.FlightSeatTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightSeatTypeServiceImpl implements FlightSeatTypeService {
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> saveFlightSeat(FlightSeatType flightSeatType) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateFlightSeat(UUID seatId, FlightSeatType flightSeatType) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removeFlightSeat(UUID seatId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getFlightSeatById(UUID seatId) {
        return null;
    }
}
