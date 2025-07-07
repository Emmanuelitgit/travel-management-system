package flight_service.rest;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightClassType;
import flight_service.models.FlightSeatType;
import flight_service.serviceImpl.FlightClassTypeServiceImpl;
import flight_service.serviceImpl.FlightSeatTypeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flight/seat-type")
public class FlightSeatTypeRest {

    private final FlightSeatTypeServiceImpl flightSeatTypeService;

    @Autowired
    public FlightSeatTypeRest(FlightSeatTypeServiceImpl flightSeatTypeService) {
        this.flightSeatTypeService = flightSeatTypeService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return flightSeatTypeService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveFlightSeat(@Valid @RequestBody FlightSeatType flightSeatType){
        return flightSeatTypeService.saveFlightSeat(flightSeatType);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<ResponseDTO> updateFlightSeat(@PathVariable UUID classId, @RequestBody FlightSeatType flightSeatType){
        return flightSeatTypeService.updateFlightSeat(classId,flightSeatType);
    }

    @GetMapping("/{seatId}")
    public ResponseEntity<ResponseDTO> getFlightSeatById(@PathVariable UUID seatId){
        return flightSeatTypeService.getFlightSeatById(seatId);
    }

    @DeleteMapping("/{seatId}")
    public ResponseEntity<ResponseDTO> removeFlightSeat(@PathVariable UUID seatId){
        return flightSeatTypeService.removeFlightSeat(seatId);
    }
}
