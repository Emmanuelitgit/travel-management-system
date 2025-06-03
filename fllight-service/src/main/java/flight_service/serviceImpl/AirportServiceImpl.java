package flight_service.serviceImpl;

import flight_service.dto.ResponseDTO;
import flight_service.exception.NotFoundException;
import flight_service.exception.ServerException;
import flight_service.models.Airport;
import flight_service.models.FlightAirlineType;
import flight_service.repo.AirportRepo;
import flight_service.service.AirportService;
import flight_service.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepo airportRepo;

    @Autowired
    public AirportServiceImpl(AirportRepo airportRepo) {
        this.airportRepo = airportRepo;
    }

    /**
     * @description Fetches all airport records from the database.
     * @return ResponseEntity containing a list of airport records and status information.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try {
            log.info("in get all airports method:->>>>");
            List<Airport> airports = airportRepo.findAll();
            if (airports.isEmpty()){
                ResponseDTO response = AppUtils.getResponseDto("no airport record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO response = AppUtils.getResponseDto("airports records", HttpStatus.OK, airports);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Saves a new airport record to the database.
     * @param airport the airport record to save.
     * @return ResponseEntity containing the saved airport record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveAirport(Airport airport) {
        try {
            log.info("in save airport record method:->>>>");
            if (airport == null){
                ResponseDTO response = AppUtils.getResponseDto("airport payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Airport res = airportRepo.save(airport);
            ResponseDTO response = AppUtils.getResponseDto("airport added successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Updates an existing airport record identified by ID.
     * @param airportId the ID of the airport to update.
     * @param airport the updated airport data.
     * @return ResponseEntity containing the updated airport record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateAirport(UUID airportId, Airport airport) {
        try {
            log.info("in update airport record method:->>>>");
            Airport existingData = airportRepo.findById(airportId)
                    .orElseThrow(()-> new NotFoundException("airline record not found"));

            existingData.setName(airport.getName() !=null? airport.getName() : existingData.getName());
            existingData.setDescription(airport.getDescription()!=null? airport.getDescription() : existingData.getDescription());
            existingData.setCity(airport.getCity()!=null? airport.getCity() : existingData.getCity());
            existingData.setCountry(airport.getCountry()!=null? airport.getCountry() : existingData.getCountry());

            Airport res = airportRepo.save(existingData);

            ResponseDTO response = AppUtils.getResponseDto("airport updated successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Deletes an airport record based on the given ID.
     * @param airportId the ID of the airport to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeAirport(UUID airportId) {
        try {
            log.info("in remove airport record method:->>>>");
            Airport airport = airportRepo.findById(airportId)
                    .orElseThrow(()-> new NotFoundException("airport record not found"));
            airportRepo.deleteById(airport.getId());

            ResponseDTO response = AppUtils.getResponseDto("airport removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Fetches a specific airport record by ID.
     * @param airportId the ID of the airport to retrieve.
     * @return ResponseEntity containing the airport record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     * */
    @Override
    public ResponseEntity<ResponseDTO> getAirportById(UUID airportId) {
        try {
            log.info("in get airport record by id method:->>>>");
            Airport airport = airportRepo.findById(airportId)
                    .orElseThrow(()-> new NotFoundException("airport record not found"));

            ResponseDTO response = AppUtils.getResponseDto("airport records fetched successfully", HttpStatus.OK, airport);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}