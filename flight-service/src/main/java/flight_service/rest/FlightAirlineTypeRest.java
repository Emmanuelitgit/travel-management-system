package flight_service.rest;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightAirlineType;
import flight_service.serviceImpl.FlightAirlineTypeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/airlines")
public class FlightAirlineTypeRest {

    private final FlightAirlineTypeServiceImpl flightAirlineTypeService;

    @Autowired
    public FlightAirlineTypeRest(FlightAirlineTypeServiceImpl flightAirlineTypeService) {
        this.flightAirlineTypeService = flightAirlineTypeService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return flightAirlineTypeService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveAirline(@Valid @RequestBody FlightAirlineType flightAirlineType){
        return flightAirlineTypeService.saveAirline(flightAirlineType);
    }

    @PutMapping("/{airlineId}")
    public ResponseEntity<ResponseDTO> updateAirline(@PathVariable UUID airlineId, @RequestBody FlightAirlineType flightAirlineType){
        return flightAirlineTypeService.updateAirline(airlineId,flightAirlineType);
    }

    @GetMapping("/{airlineId}")
    public ResponseEntity<ResponseDTO> getAirlineById(@PathVariable UUID airlineId){
        return flightAirlineTypeService.getAirlineById(airlineId);
    }

    @DeleteMapping("/{airlineId}")
    public ResponseEntity<ResponseDTO> removeAirline(@PathVariable UUID airlineId){
        return flightAirlineTypeService.removeAirline(airlineId);
    }
}
