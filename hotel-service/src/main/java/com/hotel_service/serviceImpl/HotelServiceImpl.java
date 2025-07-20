package com.hotel_service.serviceImpl;

import com.hotel_service.dto.ResponseDTO;
import com.hotel_service.exception.BadRequestException;
import com.hotel_service.exception.NotFoundException;
import com.hotel_service.exception.ServerException;
import com.hotel_service.external.ServiceCalls;
import com.hotel_service.external.dto.HotelLocationResponse;
import com.hotel_service.models.Hotel;
import com.hotel_service.repo.HotelRepo;
import com.hotel_service.service.HotelService;
import com.hotel_service.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo;
    private final ServiceCalls serviceCalls;

    @Autowired
    public HotelServiceImpl(HotelRepo hotelRepo, ServiceCalls serviceCalls) {
        this.hotelRepo = hotelRepo;
        this.serviceCalls = serviceCalls;
    }

    /**
     * @description Fetches all hotel records from the database.
     * @return ResponseEntity containing a list of hotel records and status information.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            log.info("In fetch hotel records method:->>>>");
            List<Hotel> hotels = hotelRepo.findAll();
            if (hotels.isEmpty()){
                log.info("no hotel record found->>>{}", HttpStatus.NOT_FOUND);
                throw new NotFoundException("no hotel record found");
            }

            ResponseDTO responseDTO = AppUtils.getResponseDto("hotels fetched successfully", HttpStatus.OK, hotels);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to fetch hotel record");
        }
    }

    /**
     * @description Saves a new hotel record to the database.
     * @param hotel the hotel record to save.
     * @return ResponseEntity containing the saved hotel record and status info.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveHotel(Hotel hotel) {

     try{
         log.info("In save hotel record method:->>>>");
         if (hotel == null){
             throw new BadRequestException("hotel value cannot be null");
         }

         Hotel hotelRes = hotelRepo.save(hotel);
         ResponseDTO responseDTO = AppUtils.getResponseDto("hotel added", HttpStatus.CREATED, hotelRes);
         return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

     } catch (Exception e) {
         AppUtils.logException("Message->>{}", e.getMessage());
         throw new ServerException("error occurred while trying to save hotel record");
     }

    }

    /**
     * @description Updates an existing hotel record identified by ID.
     * @param hotelId the ID of the hotel to update.
     * @param hotel the updated hotel data.
     * @return ResponseEntity containing the updated hotel record and status info.
     * @author Emmanuel Yidana
     * @createdAt 19th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateHotel(UUID hotelId, Hotel hotel) {
        try{
            log.info("In update hotel record method:->>>>");
            if (hotelId == null){
                throw new BadRequestException("hotel id cannot be null");
            }
            if (hotel == null){
                throw new BadRequestException("hotel data cannot be null");
            }

            Optional<Hotel> hotelOptional = hotelRepo.findById(hotelId);
            if (hotelOptional.isEmpty()){
                AppUtils.logException("hotel record cannot be found for id->>>>{}", String.valueOf(hotelId));
                throw new NotFoundException("hotel record cannot be found");
            }

            Hotel existingData = hotelOptional.get();
            existingData.setName(hotel.getName() !=null? hotel.getName() : existingData.getName());
            existingData.setCity(hotel.getCity() !=null? hotel.getCity() : existingData.getCity());
            existingData.setAmenities(hotel.getAmenities() !=null? hotel.getAmenities() : existingData.getAmenities());
            existingData.setProviderId(hotel.getProviderId() !=null? hotel.getProviderId() : existingData.getProviderId());
            existingData.setCountry(hotel.getCountry() !=null? hotel.getCountry() : existingData.getCountry());
            existingData.setLatitude(hotel.getLatitude() !=null? hotel.getLatitude() : existingData.getLatitude());
            existingData.setLongitude(hotel.getLongitude() !=null? hotel.getLongitude() : existingData.getLongitude());

            Hotel hotelRes = hotelRepo.save(existingData);
            ResponseDTO responseDTO = AppUtils.getResponseDto("hotel record updated", HttpStatus.OK, hotelRes);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to update hotel record");
        }
    }

    /**
     * @description Deletes a hotel record based on the given ID.
     * @param hotelId the ID of the hotel to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeHotel(UUID hotelId) {
        try {
            log.info("In delete hotel record method:->>>>");

            Optional<Hotel> hotelOptional = hotelRepo.findById(hotelId);
            if (hotelOptional.isEmpty()){
                AppUtils.logException("hotel record cannot be found for id->>>>{}", String.valueOf(hotelId));
                throw new NotFoundException("hotel record cannot be found");
            }

            hotelRepo.deleteById(hotelId);
            ResponseDTO responseDTO = AppUtils.getResponseDto("hotel record deleted", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            AppUtils.logException("Message->>{}", e.getMessage());
            throw new ServerException("error occurred while trying to delete hotel record");
        }
    }

    /**
     * @description Fetches a specific hotel record by ID.
     * @param hotelId the ID of the hotel record to retrieve.
     * @return ResponseEntity containing the hotel record and status info.
     * @author Emmanuel Yidana
     * @createdAt 19th, July 2025
     * */
    @Override
    public ResponseEntity<ResponseDTO> getHotelById(UUID hotelId) {
       try {
           log.info("In get hotel record by id method:->>>>");

           Optional<Hotel> hotelOptional = hotelRepo.findById(hotelId);
           if (hotelOptional.isEmpty()){
               AppUtils.logException("hotel record cannot be found for id->>>>{}", String.valueOf(hotelId));
               throw new NotFoundException("hotel record cannot be found");
           }

           ResponseDTO responseDTO = AppUtils.getResponseDto("hotel record retrieved", HttpStatus.OK, hotelOptional.get());
           log.info("hotel record retrieve with id->>>>{}", hotelOptional.get().getId());
           return new ResponseEntity<>(responseDTO, HttpStatus.OK);

       } catch (Exception e) {
           AppUtils.logException("Message->>{}", e.getMessage());
           throw new ServerException("error occurred while trying to retrieve hotel record");
       }
    }

    /**
     * @description Fetches a specific hotel location given the hotel name
     * @param query the name of the hotel to query
     * @return ResponseEntity containing the hotel location/address and status info.
     * @author Emmanuel Yidana
     * @createdAt 20th, July 2025
     * */
    public ResponseEntity<ResponseDTO> getHotelLocation(String query){
      try{
          HotelLocationResponse hotelLocationResponse = serviceCalls.getHotelLocation(query).block();

          if (hotelLocationResponse == null){
              log.info("no data found for the searched location->>>{}", query);
              throw new NotFoundException("no data found for the searched location");
          }

          ResponseDTO responseDTO = AppUtils.getResponseDto("hotel location", HttpStatus.OK, hotelLocationResponse);
          return new ResponseEntity<>(responseDTO, HttpStatus.OK);

      }catch (Exception e) {
          AppUtils.logException("Message->>{}", e.getMessage());
          throw new ServerException("error occurred while trying to retrieve hotel location");
      }
    }
}