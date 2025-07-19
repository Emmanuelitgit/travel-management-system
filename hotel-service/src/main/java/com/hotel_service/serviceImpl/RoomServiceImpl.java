package com.hotel_service.serviceImpl;

import com.hotel_service.dto.ResponseDTO;
import com.hotel_service.exception.AlreadyExistException;
import com.hotel_service.exception.BadRequestException;
import com.hotel_service.exception.NotFoundException;
import com.hotel_service.exception.ServerException;
import com.hotel_service.models.Room;
import com.hotel_service.repo.RoomRepo;
import com.hotel_service.service.RoomService;
import com.hotel_service.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepo roomRepo;

    @Autowired
    public RoomServiceImpl(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    /**
     * @description Fetches all room records from the database.
     * @return ResponseEntity containing a list of room records and status information.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            log.info("In fetch room records method:->>>>");
            List<Room> rooms = roomRepo.findAll();
            if (rooms.isEmpty()){
                log.info("no room record found->>>{}", HttpStatus.NOT_FOUND);
                throw new NotFoundException("no room record found");
            }

            ResponseDTO responseDTO = AppUtils.getResponseDto("rooms fetched successfully", HttpStatus.OK, rooms);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to fetch room record");
        }
    }

    /**
     * @description Saves a new room record to the database.
     * @param room the room record to save.
     * @return ResponseEntity containing the saved room record and status info.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveRoom(Room room) {
        try{
            log.info("In save room record method:->>>>");
            if (room == null){
                throw new BadRequestException("room value cannot be null");
            }
            if (room.getHotelId() == null){
                throw new BadRequestException("hotel id cannot be null");
            }

            Room roomRes = roomRepo.save(room);
            ResponseDTO responseDTO = AppUtils.getResponseDto("room added", HttpStatus.CREATED, roomRes);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to save room record");
        }
    }

    /**
     * @description Updates an existing room record identified by ID.
     * @param roomId the ID of the room to update.
     * @param room the updated room data.
     * @return ResponseEntity containing the updated room record and status info.
     * @author Emmanuel Yidana
     * @createdAt 19th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateRoom(UUID roomId, Room room) {
        try{
            log.info("In update room record method:->>>>");
            if (roomId == null){
                throw new BadRequestException("hotel id cannot be null");
            }
            if (room == null){
                throw new BadRequestException("hotel data cannot be null");
            }

            Optional<Room> roomOptional = roomRepo.findById(roomId);
            if (roomOptional.isEmpty()){
                AppUtils.logException("room record cannot be found for id->>>>{}", String.valueOf(roomId));
                throw new NotFoundException("room record cannot be found");
            }

            Room existingData = roomOptional.get();
            existingData.setCapacity(room.getCapacity() !=null? room.getCapacity() : existingData.getCapacity());
            existingData.setType(room.getType() !=null? room.getType() : existingData.getType());
            existingData.setHotelId(room.getHotelId() != null? room.getHotelId() : existingData.getHotelId());
            existingData.setIsBooked(room.getIsBooked() !=null? room.getIsBooked() : existingData.getIsBooked());
            existingData.setPrice(room.getPrice() !=null? room.getPrice() : existingData.getPrice());

            Room roomRes = roomRepo.save(existingData);
            ResponseDTO responseDTO = AppUtils.getResponseDto("room record updated", HttpStatus.OK, roomRes);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to update room record");
        }
    }

    /**
     * @description Deletes a room record based on the given ID.
     * @param roomId the ID of the room to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeRoom(UUID roomId) {
        try {
            log.info("In delete room record method:->>>>");

            Optional<Room> roomOptional = roomRepo.findById(roomId);
            if (roomOptional.isEmpty()){
                AppUtils.logException("room record cannot be found for id->>>>{}", String.valueOf(roomId));
                throw new NotFoundException("room record cannot be found");
            }

            roomRepo.deleteById(roomId);
            ResponseDTO responseDTO = AppUtils.getResponseDto("room record deleted", HttpStatus.OK);
            log.info("room record deleted with id->>>>{}", roomId);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to delete room record");
        }
    }

    /**
     * @description Fetches a specific hotel record by ID.
     * @param roomId the ID of the room record to retrieve.
     * @return ResponseEntity containing the room record and status info.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     * */
    @Override
    public ResponseEntity<ResponseDTO> getRoomById(UUID roomId) {
        try {
            log.info("In get room record by id method:->>>>");

            Optional<Room> roomOptional = roomRepo.findById(roomId);
            if (roomOptional.isEmpty()){
                AppUtils.logException("room record cannot be found for id->>>>{}", String.valueOf(roomId));
                throw new NotFoundException("room record cannot be found");
            }

            ResponseDTO responseDTO = AppUtils.getResponseDto("room record retrieved", HttpStatus.OK, roomOptional.get());
            log.info("room record retrieve with id->>>>{}", roomOptional.get().getId());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to retrieve room record");
        }
    }

    /**
     * @description this method is used to book a room given the room id.
     * @param roomId the ID of the room record to retrieve.
     * @return ResponseEntity containing the room record and status info.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     * */
    public ResponseEntity<ResponseDTO> bookRoom(UUID roomId){
        try{
            log.info("In book room method->>>>{}", roomId);

            Optional<Room> roomOptional = roomRepo.findById(roomId);
            if (roomOptional.isEmpty()){
                AppUtils.logException("room record cannot be found for id->>>>{}", String.valueOf(roomId));
                throw new NotFoundException("room record cannot be found");
            }

            Room existingData = roomOptional.get();
            // check if room already booked
            if (Boolean.TRUE.equals(existingData.getIsBooked())){
                log.info("room already booked->>>{}", roomId);
                throw new AlreadyExistException("room already booked");
            }
            // check and reduce room quantity
            if (existingData.getQuantity() == 1){
                existingData.setIsBooked(Boolean.TRUE);
                existingData.setQuantity(0);
            } else if (existingData.getQuantity()>1) {
                existingData.setQuantity(existingData.getQuantity()-1);
            }
            // saving updated info
            Room roomRes = roomRepo.save(existingData);

            ResponseDTO responseDTO = AppUtils.getResponseDto("room record retrieved", HttpStatus.OK, roomRes);
            AppUtils.logException("room record retrieve with id->>>>{}", roomOptional.get().getId().toString());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to book a room");
        }
    }

}