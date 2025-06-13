package flight_service.rest;

import flight_service.dto.PaginationPayload;
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
    public ResponseEntity<ResponseDTO> findAll(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "paginate", defaultValue = "false", required = false) boolean paginate,
            @RequestParam(name = "destination", required = false) String destination,
            @RequestParam(name = "departure", required = false) String departure,
            @RequestParam(name = "airline", required = false) String airline
    ){
        PaginationPayload paginationPayload = PaginationPayload
                .builder()
                .page(page)
                .size(size)
                .paginate(paginate)
                .airline(airline)
                .departure(departure)
                .destination(destination)
                .build();
        return flightPackageServiceImpl.findAll(paginationPayload);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveFlightPackage(@Valid @RequestBody FlightPackage flightPackage){
        return flightPackageServiceImpl.saveFlightPackage(flightPackage);
    }

    @PutMapping("/{packageId}")
    public ResponseEntity<ResponseDTO> updateFlightClass(@PathVariable UUID packageId, @RequestBody FlightPackage flightPackage){
        flightPackage.setId(packageId);
        return flightPackageServiceImpl.updateFlightPackage(flightPackage);
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
