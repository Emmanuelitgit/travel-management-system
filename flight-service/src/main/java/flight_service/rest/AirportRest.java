package flight_service.rest;

import flight_service.dto.ResponseDTO;
import flight_service.models.Airport;
import flight_service.serviceImpl.AirportServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/airports")
public class AirportRest {

    private final AirportServiceImpl airportService;

    @Autowired
    public AirportRest(AirportServiceImpl airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return airportService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveAirport(@Valid @RequestBody Airport airport){
        return airportService.saveAirport(airport);
    }

    @PutMapping("/{airportId}")
    public ResponseEntity<ResponseDTO> updateAirport(@PathVariable UUID airportId, @RequestBody Airport airport){
        return airportService.updateAirport(airportId,airport);
    }

    @GetMapping("/{airportId}")
    public ResponseEntity<ResponseDTO> getAirportById(@PathVariable UUID airportId){
        return airportService.getAirportById(airportId);
    }

    @DeleteMapping("/{airportId}")
    public ResponseEntity<ResponseDTO> removeAirport(@PathVariable UUID airportId){
        return airportService.removeAirport(airportId);
    }
}