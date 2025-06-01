package flight_service.serviceImpl;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightClassType;
import flight_service.service.FlightClassTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightClassTypeServiceImpl implements FlightClassTypeService {
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> saveFlightClass(FlightClassType flightClassType) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateFlightClass(UUID classId, FlightClassType flightClassType) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removeFlightClass(UUID classId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getFlightClassById(UUID classId) {
        return null;
    }
}
