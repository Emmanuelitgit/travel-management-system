package flight_service.serviceImpl;

import flight_service.dto.ResponseDTO;
import flight_service.exception.NotFoundException;
import flight_service.exception.ServerException;
import flight_service.models.FlightAirlineType;
import flight_service.repo.FlightAirlineTypeRepo;
import flight_service.service.FlightAirlineTypeService;
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
public class FlightAirlineTypeServiceImpl implements FlightAirlineTypeService {

    private final FlightAirlineTypeRepo flightAirlineTypeRepo;

    @Autowired
    public FlightAirlineTypeServiceImpl(FlightAirlineTypeRepo flightAirlineTypeRepo) {
        this.flightAirlineTypeRepo = flightAirlineTypeRepo;
    }

    /**
     * @description Fetches all airline records from the database.
     * @return ResponseEntity containing a list of airline records and status information.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        log.info("in get all airlines method:->>>>");
        try {
            List<FlightAirlineType> flightAirlineTypes = flightAirlineTypeRepo.findAll();
            if (flightAirlineTypes.isEmpty()){
                ResponseDTO response = AppUtils.getResponseDto("no airline record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            ResponseDTO response = AppUtils.getResponseDto("airlines records", HttpStatus.OK, flightAirlineTypes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Saves a new airline record to the database.
     * @param flightAirlineType the airline record to save.
     * @return ResponseEntity containing the saved airline record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveAirline(FlightAirlineType flightAirlineType) {
        try {
            log.info("in save airline record method:->>>>");
            if (flightAirlineType == null){
                ResponseDTO response = AppUtils.getResponseDto("airline payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            FlightAirlineType res = flightAirlineTypeRepo.save(flightAirlineType);
            ResponseDTO response = AppUtils.getResponseDto("airline added successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Updates an existing airline record identified by ID.
     * @param airlineId the ID of the airline to update.
     * @param flightAirlineType the updated airline data.
     * @return ResponseEntity containing the updated airline record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateAirline(UUID airlineId, FlightAirlineType flightAirlineType) {
        try {
            log.info("in update airline record method:->>>>");
            FlightAirlineType existingData = flightAirlineTypeRepo.findById(airlineId)
                    .orElseThrow(()-> new NotFoundException("airline record not found"));

            existingData.setName(flightAirlineType.getName() !=null? flightAirlineType.getName() : existingData.getName());
            existingData.setDescription(flightAirlineType.getDescription()!=null? flightAirlineType.getDescription() : existingData.getDescription());
            existingData.setActive(flightAirlineType.isActive());

            FlightAirlineType res = flightAirlineTypeRepo.save(existingData);

            ResponseDTO response = AppUtils.getResponseDto("airline added successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Deletes an airline record based on the given ID.
     * @param airlineId the ID of the airline to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeAirline(UUID airlineId) {
        try {
            log.info("in remove airline record method:->>>>");
            FlightAirlineType flightAirlineType = flightAirlineTypeRepo.findById(airlineId)
                    .orElseThrow(()-> new NotFoundException("airline record not found"));
            flightAirlineTypeRepo.deleteById(flightAirlineType.getId());

            ResponseDTO response = AppUtils.getResponseDto("airline removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Fetches a specific airline record by ID.
     * @param airlineId the ID of the airline to retrieve.
     * @return ResponseEntity containing the airline record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     * */
    @Override
    public ResponseEntity<ResponseDTO> getAirlineById(UUID airlineId) {
       try {
           log.info("in get airline record by id method:->>>>");
           FlightAirlineType flightAirlineType = flightAirlineTypeRepo.findById(airlineId)
                   .orElseThrow(()-> new NotFoundException("airline record not found"));

           ResponseDTO response = AppUtils.getResponseDto("airline records fetched successfully", HttpStatus.OK, flightAirlineType);
           return new ResponseEntity<>(response, HttpStatus.OK);
       } catch (Exception e) {
           throw new ServerException(e.getMessage());
       }
    }
}
