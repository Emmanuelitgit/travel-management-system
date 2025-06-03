package flight_service.serviceImpl;

import flight_service.dto.ResponseDTO;
import flight_service.exception.NotFoundException;
import flight_service.exception.ServerException;
import flight_service.models.FlightAirlineType;
import flight_service.models.FlightClassType;
import flight_service.repo.FlightClassTypeRepo;
import flight_service.service.FlightClassTypeService;
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
public class FlightClassTypeServiceImpl implements FlightClassTypeService {
    private final FlightClassTypeRepo flightClassTypeRepo;

    @Autowired
    public FlightClassTypeServiceImpl(FlightClassTypeRepo flightClassTypeRepo) {
        this.flightClassTypeRepo = flightClassTypeRepo;
    }

    /**
     * @description Fetches all flight class records from the database.
     * @return ResponseEntity containing a list of flight class records and status information.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        log.info("in get all flight classes method:->>>>");
        try {
            List<FlightClassType> flightClassTypes = flightClassTypeRepo.findAll();
            if (flightClassTypes.isEmpty()){
                ResponseDTO response = AppUtils.getResponseDto("no flight class record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            ResponseDTO response = AppUtils.getResponseDto("flight class records", HttpStatus.OK, flightClassTypes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Saves a new flight class record to the database.
     * @param flightClassType the flight class record to save.
     * @return ResponseEntity containing the saved flight class record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveFlightClass(FlightClassType flightClassType) {
        try {
            log.info("in save flight class record method:->>>>");
            if (flightClassType == null){
                ResponseDTO response = AppUtils.getResponseDto("flight class payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            FlightClassType res = flightClassTypeRepo.save(flightClassType);
            ResponseDTO response = AppUtils.getResponseDto("flight class added successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Updates an existing flight class record identified by ID.
     * @param classId the ID of the flight class to update.
     * @param flightClassType the updated flight class data.
     * @return ResponseEntity containing the updated flight class record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateFlightClass(UUID classId, FlightClassType flightClassType) {
        try {
            log.info("in update flight class record method:->>>>");
            FlightClassType existingData = flightClassTypeRepo.findById(classId)
                    .orElseThrow(()-> new NotFoundException("flight class record not found"));

            existingData.setName(flightClassType.getName() !=null? flightClassType.getName() : existingData.getName());
            existingData.setDescription(flightClassType.getDescription()!=null? flightClassType.getDescription() : existingData.getDescription());
            existingData.setActive(flightClassType.isActive());

            FlightClassType res = flightClassTypeRepo.save(existingData);

            ResponseDTO response = AppUtils.getResponseDto("flight class added successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Deletes a flight class record based on the given ID.
     * @param classId the ID of the flight class to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeFlightClass(UUID classId) {
        try {
            log.info("in remove flight class record method:->>>>");
            FlightClassType flightClassType = flightClassTypeRepo.findById(classId)
                    .orElseThrow(()-> new NotFoundException("flight class record not found"));
            flightClassTypeRepo.deleteById(flightClassType.getId());

            ResponseDTO response = AppUtils.getResponseDto("flight class removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Fetches a specific flight class record by ID.
     * @param classId the ID of the flight class to retrieve.
     * @return ResponseEntity containing the flight class record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     * */
    @Override
    public ResponseEntity<ResponseDTO> getFlightClassById(UUID classId) {
        try {
            log.info("in get flight class record by id method:->>>>");
            FlightClassType flightClassType = flightClassTypeRepo.findById(classId)
                    .orElseThrow(()-> new NotFoundException("flight class record not found"));

            ResponseDTO response = AppUtils.getResponseDto("flight class records fetched successfully", HttpStatus.OK, flightClassType);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
