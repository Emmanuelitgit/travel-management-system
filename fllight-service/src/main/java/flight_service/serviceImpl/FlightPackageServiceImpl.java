package flight_service.serviceImpl;

import flight_service.dto.ResponseDTO;
import flight_service.dto.enums.TripType;
import flight_service.dto.projections.FlightPackageProjection;
import flight_service.exception.BadRequestException;
import flight_service.exception.NotFoundException;
import flight_service.exception.ServerException;
import flight_service.models.FlightClassType;
import flight_service.models.FlightPackage;
import flight_service.repo.*;
import flight_service.service.FlightPackageService;
import flight_service.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FlightPackageServiceImpl implements FlightPackageService {

    private final FlightPackageRepo flightPackageRepo;
    private final FlightClassTypeRepo flightClassTypeRepo;
    private final FlightSeatTypeRepo flightSeatTypeRepo;
    private final AirportRepo airportRepo;
    private final FlightAirlineTypeRepo flightAirlineTypeRepo;

    @Autowired
    public FlightPackageServiceImpl(FlightPackageRepo flightPackageRepo, FlightClassTypeRepo flightClassTypeRepo, FlightSeatTypeRepo flightSeatTypeRepo, AirportRepo airportRepo, FlightAirlineTypeRepo flightAirlineTypeRepo) {
        this.flightPackageRepo = flightPackageRepo;
        this.flightClassTypeRepo = flightClassTypeRepo;
        this.flightSeatTypeRepo = flightSeatTypeRepo;
        this.airportRepo = airportRepo;
        this.flightAirlineTypeRepo = flightAirlineTypeRepo;
    }


    /**
     * @description Fetches all flight package records from the database.
     * @return ResponseEntity containing a list of flight package records and status information.
     * @author Emmanuel Yidana
     * @createdAt 3rd, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try {
            List<FlightPackageProjection> flightPackageProjectionList = flightPackageRepo.fetchAllFlightPackages();
            log.info("packages:->>>>{}", flightPackageProjectionList);
            if (flightPackageProjectionList.isEmpty()){
                throw new NotFoundException("no flight package record found");
            }

            ResponseDTO response = AppUtils.getResponseDto("flight package records", HttpStatus.OK, flightPackageProjectionList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Saves a new flight package record to the database.
     * @param flightPackage the flight package record to save.
     * @return ResponseEntity containing the saved flight package record and status info.
     * @author Emmanuel Yidana
     * @createdAt 3rd, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveFlightPackage(FlightPackage flightPackage) {
        try {
            log.info("in save flight package record method:->>>>");
            if (flightPackage == null){
                ResponseDTO response = AppUtils.getResponseDto("flight package payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // checking flight details existence
            checkFlightDetailsExistence(flightPackage);

            flightPackage.setTripType(flightPackage.getTripType().toUpperCase());
            FlightPackage res = flightPackageRepo.save(flightPackage);
            ResponseDTO response = AppUtils.getResponseDto("flight package created successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Updates an existing flight package record identified by ID.
     * @param packageId the ID of the flight package to update.
     * @param flightPackage the updated flight package data.
     * @return ResponseEntity containing the updated flight package record and status info.
     * @author Emmanuel Yidana
     * @createdAt 3rd, June 2025
     @Override
     */
    public ResponseEntity<ResponseDTO> updateFlightPackage(UUID packageId, FlightPackage flightPackage) {
        try {
            log.info("in update flight package record method:->>>>");
            FlightPackage existingData = flightPackageRepo.findById(packageId)
                    .orElseThrow(()-> new NotFoundException("flight package record not found"));

            existingData.setAirlineId(flightPackage.getAirlineId() !=null?flightPackage.getAirlineId():existingData.getAirlineId());
            existingData.setDeparture(flightPackage.getDeparture() !=null? flightPackage.getDeparture() : existingData.getDeparture());
            existingData.setDestination(flightPackage.getDestination() !=null? flightPackage.getDestination() : existingData.getDestination());
            existingData.setClassType(flightPackage.getClassType() !=null? flightPackage.getClassType() : existingData.getClassType());
            existingData.setArrivalDate(flightPackage.getArrivalDate() !=null? flightPackage.getArrivalDate() : existingData.getArrivalDate());
            existingData.setAvailableSeats(flightPackage.getAvailableSeats() !=null? flightPackage.getAvailableSeats(): existingData.getAvailableSeats());
            existingData.setDepartureDate(flightPackage.getDepartureDate() !=null? flightPackage.getDepartureDate() : existingData.getDepartureDate());
            existingData.setDescription(flightPackage.getDescription() !=null? flightPackage.getDescription() : existingData.getDescription());
            existingData.setTripType(flightPackage.getTripType() !=null? flightPackage.getTripType() : existingData.getTripType());
            existingData.setNonStop(flightPackage.isNonStop());
            existingData.setSeatType(flightPackage.getSeatType() !=null? flightPackage.getSeatType() : existingData.getSeatType());

            // checking flight details existence
            checkFlightDetailsExistence(existingData);

            // saving updated flight package details
            FlightPackage res = flightPackageRepo.save(existingData);

            ResponseDTO response = AppUtils.getResponseDto("flight package updated successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description A helper method used to check flight details existence such as airline, airport, seatType, classType given their IDs.
     * @param flightPackage the parameter containing the various payloads.
     * @return void but throws not found exception if one of the details not found.
     * @author Emmanuel Yidana
     * @createdAt 4th, June 2025
     */
    private void checkFlightDetailsExistence(FlightPackage flightPackage){
        // check if seat type exist
        flightSeatTypeRepo.findById(flightPackage.getSeatType())
                .orElseThrow(()-> new NotFoundException("flight seat type record not found"));
        // check if departure exist
        airportRepo.findById(flightPackage.getDeparture())
                .orElseThrow(()-> new NotFoundException("departure record not found"));
        // check if destination exist
        airportRepo.findById(flightPackage.getDestination())
                .orElseThrow(()-> new NotFoundException("destination record not found"));
        // check if airline exist
        flightAirlineTypeRepo.findById(flightPackage.getAirlineId())
                .orElseThrow(()-> new NotFoundException("airline type record not found"));
        // check if class type exist
        flightClassTypeRepo.findById(flightPackage.getClassType())
                .orElseThrow(()-> new NotFoundException("flight class type record not found"));

        // validate departure date and arrival date
        if (flightPackage.getDepartureDate().isEqual(flightPackage.getArrivalDate())){
            throw new BadRequestException("departure date cannot be same as arrival date");
        }
        if (flightPackage.getDepartureDate().isAfter(flightPackage.getArrivalDate())){
            throw new BadRequestException("departure date cannot be ahead of arrival date");
        }
        if (!flightPackage.getTripType().equalsIgnoreCase(TripType.ROUND_TRIP.toString()) && !flightPackage.getTripType().equalsIgnoreCase(TripType.ONE_WAY.toString())){
            throw new BadRequestException("Invalid trip type: must be ONE_WAY or ROUND_TRIP.");
        }
        if (flightPackage.getDeparture().equals(flightPackage.getDestination())){
            throw new BadRequestException("departure airport and destination airport cannot be same");
        }
    }

    /**
     * @description Deletes a flight package record based on the given ID.
     * @param packageId the ID of the flight package  to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 3rd, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeFlightPackage(UUID packageId) {

        try {
            log.info("in remove flight package record method:->>>>");
            FlightPackage flightPackage = flightPackageRepo.findById(packageId)
                    .orElseThrow(()-> new NotFoundException("flight package record not found"));
            flightPackageRepo.deleteById(flightPackage.getId());

            ResponseDTO response = AppUtils.getResponseDto("flight package removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Fetches a specific flight package record by ID.
     * @param packageId the ID of the flight package to retrieve.
     * @return ResponseEntity containing the flight package record and status info.
     * @author Emmanuel Yidana
     * @createdAt 3rd, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> getFlightPackageById(UUID packageId) {
        try {
            log.info("in get flight package record by id method:->>>>");
            flightPackageRepo.findById(packageId)
                    .orElseThrow(()-> new NotFoundException("flight package record not found"));

            FlightPackageProjection flightPackage = flightPackageRepo.findFlightPackageById(packageId);

            ResponseDTO response = AppUtils.getResponseDto("flight package records fetched successfully", HttpStatus.OK, flightPackage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
