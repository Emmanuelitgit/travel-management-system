package booking_service.serviceImpl;

import booking_service.dto.BookingProjection;
import booking_service.dto.ResponseDTO;
import booking_service.exception.BadRequestException;
import booking_service.exception.NotFoundException;
import booking_service.exception.ServerException;
import booking_service.external.ServiceCalls;
import booking_service.models.Booking;
import booking_service.repo.BookingRepo;
import booking_service.service.BookingService;
import booking_service.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final ServiceCalls serviceCalls;

    @Autowired
    public BookingServiceImpl(BookingRepo bookingRepo, ServiceCalls serviceCalls) {
        this.bookingRepo = bookingRepo;
        this.serviceCalls = serviceCalls;
    }

    /**
     * @description Fetches all bookings records from the database.
     * @return ResponseEntity containing a list of bookings records and status information.
     * @author Emmanuel Yidana
     * @createdAt 4th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
       try {
           log.info("in fetch all bookings records method:->>>>");
           List<BookingProjection> bookings = bookingRepo.fetchAllBookings();
           if (bookings.isEmpty()){
               throw new NotFoundException("no booking record found");
           }
           ResponseDTO response = AppUtils.getResponseDto("airports records", HttpStatus.OK, bookings);
           return new ResponseEntity<>(response, HttpStatus.OK);
       }catch (NotFoundException e) {
           throw new NotFoundException(e.getMessage());
       } catch (Exception e) {
           throw new ServerException(e.getMessage());
       }
    }

    /**
     * @description Saves a new booking record to the database.
     * @param booking the booking record to save.
     * @return ResponseEntity containing the saved booking record and status info.
     * @author Emmanuel Yidana
     * @createdAt 4th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> saveBooking(Booking booking) {

        try {
            log.info("in save booking record method:->>>>");
            if (booking == null){
                ResponseDTO response = AppUtils.getResponseDto("airport payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            serviceCalls.getFlightPackage(booking.getPackageId())
                    .subscribe((response)->{
                        if(response.getAvailableSeats() < 1){
                            throw new BadRequestException("flight is occupied already");
                        }
                    });

            Booking res = bookingRepo.save(booking);
            ResponseDTO response = AppUtils.getResponseDto("booking record added successfully", HttpStatus.CREATED, res);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Updates an existing booking record identified by ID.
     * @param bookingId the ID of the booking to update.
     * @param booking the updated booking data.
     * @return ResponseEntity containing the updated booking record and status info.
     * @author Emmanuel Yidana
     * @createdAt 4th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateBooking(UUID bookingId, Booking booking) {

        try {
            log.info("in update booking record method:->>>>");
            Booking existingData = bookingRepo.findById(bookingId)
                    .orElseThrow(()-> new NotFoundException("booking record not found"));

            existingData.setBookingStatus(booking.getBookingStatus() !=null? booking.getBookingStatus() : existingData.getBookingStatus());
            existingData.setPackageId(booking.getPackageId() !=null? booking.getPackageId() : existingData.getPackageId());
            existingData.setTotalPrice(booking.getTotalPrice() !=null? booking.getTotalPrice() : existingData.getTotalPrice());
            existingData.setSeatNumber(booking.getSeatNumber() !=null? booking.getSeatNumber() : existingData.getSeatNumber());
            existingData.setUserId(booking.getUserId() !=null? booking.getUserId() : existingData.getUserId());
            existingData.setPaymentStatus(booking.getPaymentStatus() !=null? booking.getPaymentStatus() : existingData.getPaymentStatus());

            Booking res = bookingRepo.save(existingData);

            ResponseDTO response = AppUtils.getResponseDto("booking updated successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Deletes an booking record based on the given ID.
     * @param bookingId the ID of the booking to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 4th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeBooking(UUID bookingId) {
        try {
            log.info("in remove booking record method:->>>>");
            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(()-> new NotFoundException("booking record not found"));
            bookingRepo.deleteById(booking.getId());

            ResponseDTO response = AppUtils.getResponseDto("booking removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> cancelBooking(UUID bookingId) {
        return null;
    }

    /**
     * @description Fetches a specific booking record by ID.
     * @param bookingId the ID of the booking to retrieve.
     * @return ResponseEntity containing the booking record and status info.
     * @author Emmanuel Yidana
     * @createdAt 4th, June 2025
     * */
    @Override
    public ResponseEntity<ResponseDTO> getBookingById(UUID bookingId) {
        try {
            log.info("in get booking record by id method:->>>>");
            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(()-> new NotFoundException("booking record not found"));

            ResponseDTO response = AppUtils.getResponseDto("airport records fetched successfully", HttpStatus.OK, booking);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
