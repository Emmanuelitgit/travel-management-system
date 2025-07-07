package flight_service.rest;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightClassType;
import flight_service.serviceImpl.FlightClassTypeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flight-class")
public class FlightClassTypeRest {

    private final FlightClassTypeServiceImpl flightClassTypeService;

    @Autowired
    public FlightClassTypeRest(FlightClassTypeServiceImpl flightClassTypeService) {
        this.flightClassTypeService = flightClassTypeService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return flightClassTypeService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveFlightClass(@Valid @RequestBody FlightClassType flightClassType){
        return flightClassTypeService.saveFlightClass(flightClassType);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<ResponseDTO> updateFlightClass(@PathVariable UUID classId, @RequestBody FlightClassType flightClassType){
        return flightClassTypeService.updateFlightClass(classId,flightClassType);
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ResponseDTO> getFlightClassById(@PathVariable UUID classId){
        return flightClassTypeService.getFlightClassById(classId);
    }

    @DeleteMapping("/{airportId}")
    public ResponseEntity<ResponseDTO> removeFlightClass(@PathVariable UUID airportId){
        return flightClassTypeService.removeFlightClass(airportId);
    }
}
