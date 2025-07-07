package flight_service.serviceImpl;

import flight_service.dto.ResponseDTO;
import flight_service.exception.NotFoundException;
import flight_service.exception.ServerException;
import flight_service.models.FlightClassType;
import flight_service.models.FlightSeatType;
import flight_service.repo.FlightSeatTypeRepo;
import flight_service.service.FlightSeatTypeService;
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
public class FlightSeatTypeServiceImpl implements FlightSeatTypeService {

    private final FlightSeatTypeRepo flightSeatTypeRepo;

    @Autowired
    public FlightSeatTypeServiceImpl(FlightSeatTypeRepo flightSeatTypeRepo) {
        this.flightSeatTypeRepo = flightSeatTypeRepo;
    }

    /**
     * @description Fetches all flight seat type records from the database.
     * @return ResponseEntity containing a list of flight seat types records and status information.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try {
            log.info("in get all flight seat types method:->>>>");
            List<FlightSeatType> flightSeatTypes = flightSeatTypeRepo.findAll();
            if (flightSeatTypes.isEmpty()){
                ResponseDTO response = AppUtils.getResponseDto("no flight seat record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            ResponseDTO response = AppUtils.getResponseDto("flight class records", HttpStatus.OK, flightSeatTypes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Saves a new flight seat type record to the database.
     * @param flightSeatType the flight seat type record to save.
     * @return ResponseEntity containing the saved flight seat type record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveFlightSeat(FlightSeatType flightSeatType) {
        try {
            log.info("in save flight seat type record method:->>>>");
            if (flightSeatType == null){
                ResponseDTO response = AppUtils.getResponseDto("flight seat type payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            FlightSeatType res = flightSeatTypeRepo.save(flightSeatType);
            ResponseDTO response = AppUtils.getResponseDto("flight seat type added successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Updates an existing flight seat type record identified by ID.
     * @param seatId the ID of the flight seat type to update.
     * @param flightSeatType the updated flight seat type data.
     * @return ResponseEntity containing the updated flight seat type record and status info.
     * @author Emmanuel Yidana
     * @createdAt 1st, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateFlightSeat(UUID seatId, FlightSeatType flightSeatType) {
        try {
            log.info("in update flight seat type record method:->>>>");
            FlightSeatType existingData = flightSeatTypeRepo.findById(seatId)
                    .orElseThrow(()-> new NotFoundException("flight seat type record not found"));

            existingData.setName(flightSeatType.getName() !=null? flightSeatType.getName() : existingData.getName());
            existingData.setDescription(flightSeatType.getDescription()!=null? flightSeatType.getDescription() : existingData.getDescription());
            existingData.setActive(flightSeatType.isActive());

            FlightSeatType res = flightSeatTypeRepo.save(existingData);

            ResponseDTO response = AppUtils.getResponseDto("flight seat type updated successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Deletes a flight seat type record based on the given ID.
     * @param seatId the ID of the flight seat type to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 2nd, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeFlightSeat(UUID seatId) {
        try {
            log.info("in remove flight seat type record method:->>>>");
            FlightSeatType flightSeatType = flightSeatTypeRepo.findById(seatId)
                    .orElseThrow(()-> new NotFoundException("flight seat type record not found"));
            flightSeatTypeRepo.deleteById(flightSeatType.getId());

            ResponseDTO response = AppUtils.getResponseDto("flight seat type removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Fetches a specific flight seat type record by ID.
     * @param seatId the ID of the flight seat type to retrieve.
     * @return ResponseEntity containing the flight seat type record and status info.
     * @author Emmanuel Yidana
     * @createdAt 2nd, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> getFlightSeatById(UUID seatId) {
        try {
            log.info("in get flight seat type record by id method:->>>>");
            FlightSeatType flightClassType = flightSeatTypeRepo.findById(seatId)
                    .orElseThrow(()-> new NotFoundException("flight seat type record not found"));

            ResponseDTO response = AppUtils.getResponseDto("flight seat type records fetched successfully", HttpStatus.OK, flightClassType);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
