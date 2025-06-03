package flight_service.rest;

import flight_service.dto.ResponseDTO;
import flight_service.models.FlightClassType;
import flight_service.models.FlightPackage;
import flight_service.serviceImpl.FlightPackageServiceImpl;
import flight_service.serviceImpl.FlightSeatTypeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flight-package")
public class FlightPackageRest {

    private final FlightPackageServiceImpl flightPackageServiceImpl;

    @Autowired
    public FlightPackageRest(FlightPackageServiceImpl flightPackageServiceImpl) {
        this.flightPackageServiceImpl = flightPackageServiceImpl;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return flightPackageServiceImpl.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveFlightPackage(@Valid @RequestBody FlightPackage flightPackage){
        return flightPackageServiceImpl.saveFlightPackage(flightPackage);
    }

    @PutMapping("/{packageId}")
    public ResponseEntity<ResponseDTO> updateFlightClass(@PathVariable UUID packageId, @RequestBody FlightPackage flightPackage){
        return flightPackageServiceImpl.updateFlightPackage(packageId, flightPackage);
    }

    @GetMapping("/{packageId}")
    public ResponseEntity<ResponseDTO> getFlightClassById(@PathVariable UUID packageId){
        return flightPackageServiceImpl.getFlightPackageById(packageId);
    }

    @DeleteMapping("/{packageId}")
    public ResponseEntity<ResponseDTO> removeFlightClass(@PathVariable UUID packageId){
        return flightPackageServiceImpl.removeFlightPackage(packageId);
    }
}
