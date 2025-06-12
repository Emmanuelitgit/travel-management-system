package booking_service.serviceImpl;

import booking_service.config.kafka.dto.BookingNotificationPayload;
import booking_service.config.kafka.dto.BookingUpdatePayload;
import booking_service.config.kafka.dto.FlightUpdatePayload;
import booking_service.config.kafka.dto.PaymentUpdatePayload;
import booking_service.dto.BookingResponse;
import booking_service.dto.ResponseDTO;
import booking_service.dto.enums.BookingStatus;
import booking_service.dto.enums.PaymentStatus;
import booking_service.exception.BadRequestException;
import booking_service.exception.NotFoundException;
import booking_service.exception.ServerException;
import booking_service.external.ServiceCalls;
import booking_service.external.dto.FlightPackageResponse;
import booking_service.external.dto.UserResponse;
import booking_service.models.Booking;
import booking_service.repo.BookingRepo;
import booking_service.service.BookingService;
import booking_service.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final ServiceCalls serviceCalls;
    private final KafkaTemplate<String, PaymentUpdatePayload> paymentUpdatekafkaTemplate;
    private final KafkaTemplate<String, FlightUpdatePayload> flightPackageUpdatekafkaTemplate;
    private final KafkaTemplate<String, BookingNotificationPayload> bookingNotificationkafkaTemplate;

    @Autowired
    public BookingServiceImpl(BookingRepo bookingRepo, ServiceCalls serviceCalls, KafkaTemplate<String, PaymentUpdatePayload> paymentUpdatekafkaTemplate, KafkaTemplate<String, FlightUpdatePayload> flightPackageUpdatekafkaTemplate, KafkaTemplate<String, BookingNotificationPayload> bookingNotificationkafkaTemplate) {
        this.bookingRepo = bookingRepo;
        this.serviceCalls = serviceCalls;
        this.paymentUpdatekafkaTemplate = paymentUpdatekafkaTemplate;
        this.flightPackageUpdatekafkaTemplate = flightPackageUpdatekafkaTemplate;
        this.bookingNotificationkafkaTemplate = bookingNotificationkafkaTemplate;
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
           List<Booking> bookings = bookingRepo.findAll();
           if (bookings.isEmpty()){
               throw new NotFoundException("no booking record found");
           }

           List<BookingResponse> responses = new ArrayList<>();
           bookings.forEach((booking)->{

               // make request to fetch user details
               log.info("About to fetch user details:->>>>");
               UserResponse userResponse = serviceCalls.getUserInfo(booking.getUserId()).block();

               // make request to fetch flight details
               log.info("About to fetch flight details:->>>>");
               FlightPackageResponse flightPackageResponse = serviceCalls.getFlightPackage(booking.getPackageId()).block();

               log.info("Binding booking response:->>>");
               BookingResponse bookingResponse = BookingResponse
                       .builder()
                       .bookingStatus(booking.getBookingStatus())
                       .paymentStatus(booking.getPaymentStatus())
                       .totalPrice(booking.getTotalPrice())
                       .seatNumber(booking.getSeatNumber())
                       .bookingId(booking.getId())
                       .user(userResponse)
                       .flight(flightPackageResponse)
                       .build();

               responses.add(bookingResponse);
           });

           ResponseDTO response = AppUtils.getResponseDto("airports records", HttpStatus.OK, responses);
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
            log.info("in save booking record method");

            // Validate payload
            if (booking == null) {
                ResponseDTO response = AppUtils.getResponseDto("booking payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Check if seat already booked for
            Optional<Booking> seatExist = bookingRepo.findBySeatNumber(booking.getSeatNumber());
            if (seatExist.isPresent()) {
                throw new BadRequestException("Seat is already booked");
            }

            // Check flight package availability via WebClient (block for response)
            log.info("About to make request to flight-service:->>>>");
            FlightPackageResponse flightResponse = serviceCalls
                    .getFlightPackage(booking.getPackageId())
                    .block(); // block to wait for response

            if (flightResponse == null){
               throw new BadRequestException("flight package cannot be null");
            }

            if (flightResponse.getAvailableSeats() < 1) {
                throw new BadRequestException("Flight is already occupied");
            }

            if (flightResponse.getAvailableSeats() < booking.getNumberOfSeats()) {
                throw new BadRequestException("Requested seats exceeds the available seats");
            }

            // publish an update to update flight package to reduce available seats
            log.info("About to publish an update to payment service:->>>>");
            FlightUpdatePayload flightUpdatePayload = FlightUpdatePayload
                    .builder()
                    .id(flightResponse.getId())
                    .availableSeats(flightResponse.getAvailableSeats()-booking.getNumberOfSeats())
                    .build();
            flightPackageUpdatekafkaTemplate.send("flightPackageUpdate", flightUpdatePayload);
//            serviceCalls.updateFlightPackage(booking.getPackageId(), flightResponse.getAvailableSeats()-booking.getNumberOfSeats()).block();

            // Save the booking in DB
            log.info("About to save booking record to db:->>>>>");
            Float totalPrice = booking.getNumberOfSeats()*flightResponse.getPrice();
            booking.setTotalPrice(totalPrice);
            booking.setBookingStatus(BookingStatus.PENDING.toString());
            booking.setPaymentStatus(PaymentStatus.PENDING.toString());
            Booking savedBooking = bookingRepo.save(booking);

            // get user details
            UserResponse userResponse = serviceCalls
                    .getUserInfo(savedBooking.getUserId())
                    .block();

            if (userResponse == null){
                throw new NotFoundException("user record not found to process booking");
            }

            // publish payment update to process payment
            log.info("About to publish an update to payment service:->>>");
            PaymentUpdatePayload paymentUpdatePayload = PaymentUpdatePayload
                    .builder()
                    .email(userResponse.getEmail())
                    .amount(savedBooking.getTotalPrice())
                    .bookingId(savedBooking.getId())
                    .build();
            paymentUpdatekafkaTemplate.send("hey", paymentUpdatePayload);

            // Prepare and return success response
            log.info("Booking was successfully:->>>>>");
            ResponseDTO response = AppUtils.getResponseDto("Booking added successfully", HttpStatus.CREATED, savedBooking);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ServerException e) {
            throw new ServerException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException("Internal error occurred: " + e.getMessage());
        }
    }


    /**
     * @description Updates an existing booking record identified by ID.
     * @param booking the updated booking data.
     * @return ResponseEntity containing the updated booking record and status info.
     * @author Emmanuel Yidana
     * @createdAt 4th, June 2025
     */
    @KafkaListener(topics = "bookingUpdate", containerFactory = "bookingUpdateKafkaListenerContainerFactory", groupId = "booking-group")
    @Override
    public ResponseEntity<ResponseDTO> updateBooking(Booking booking) {

        try {
            log.info("in update booking record method:->>>>");
            Booking existingData = bookingRepo.findById(booking.getId())
                    .orElseThrow(()-> new NotFoundException("booking record not found"));

            existingData.setBookingStatus(booking.getBookingStatus() !=null? booking.getBookingStatus().toUpperCase() : existingData.getBookingStatus());
            existingData.setPackageId(booking.getPackageId() !=null? booking.getPackageId() : existingData.getPackageId());
            existingData.setTotalPrice(booking.getTotalPrice() !=null? booking.getTotalPrice() : existingData.getTotalPrice());
            existingData.setSeatNumber(booking.getSeatNumber() !=null? booking.getSeatNumber() : existingData.getSeatNumber());
            existingData.setUserId(booking.getUserId() !=null? booking.getUserId() : existingData.getUserId());
            existingData.setPaymentStatus(booking.getPaymentStatus() !=null? booking.getPaymentStatus().toUpperCase() : existingData.getPaymentStatus());
            existingData.setNumberOfSeats(booking.getNumberOfSeats() !=null? booking.getNumberOfSeats() : existingData.getNumberOfSeats());

            Booking res = bookingRepo.save(existingData);

           if (
                   booking.getBookingStatus().equalsIgnoreCase(BookingStatus.CONFIRMED.toString()) &&
                   booking.getPaymentStatus().equalsIgnoreCase(PaymentStatus.PAID.toString())
           ){
                // get user details
                UserResponse userResponse = serviceCalls
                        .getUserInfo(existingData.getUserId())
                        .block();

                if (userResponse == null){
                    throw new NotFoundException("user record not found to process booking");
                }

                // get flight package details
               log.info("About to make request to flight-service:->>>>");
               FlightPackageResponse flightResponse = serviceCalls
                        .getFlightPackage(booking.getPackageId())
                        .block(); // block to wait for response

                if (flightResponse == null){
                    throw new NotFoundException("flight record not found to process booking");
                }

                // publish an update to the notification service to send confirmation notification to user
               log.info("About to publish an update to notification service:->>>>");
               BookingNotificationPayload bookingNotificationPayload = BookingNotificationPayload
                        .builder()
                        .email(userResponse.getEmail())
                        .airline(flightResponse.getAirline())
                        .arrivalDate(flightResponse.getArrivalDate())
                        .classType(flightResponse.getClassType())
                        .departure(flightResponse.getDeparture())
                        .departureDate(flightResponse.getDepartureDate())
                        .price(flightResponse.getPrice())
                        .destination(flightResponse.getDestination())
                        .firstName(userResponse.getFirstName())
                        .lastName(userResponse.getLastName())
                        .numberOfSeats(existingData.getNumberOfSeats())
                        .tripType(flightResponse.getTripType())
                        .totalPrice(flightResponse.getPrice())
                        .seatNumber(existingData.getSeatNumber())
                        .arrivalDate(flightResponse.getArrivalDate())
                        .build();
               bookingNotificationkafkaTemplate.send("bookingNotification",bookingNotificationPayload);
            }

           log.info("Booking update was successfully");
            ResponseDTO response = AppUtils.getResponseDto("booking updated successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Deletes a booking record based on the given ID.
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

            // make request to fetch user details
            log.info("About to fetch user details:->>>>");
            UserResponse userResponse = serviceCalls.getUserInfo(booking.getUserId()).block();

            // make a request to get flight details
            log.info("About to fetch flight details:->>>");
            FlightPackageResponse flightPackageResponse = serviceCalls.getFlightPackage(booking.getPackageId()).block();

            BookingResponse bookingResponse = BookingResponse
                    .builder()
                    .bookingStatus(booking.getBookingStatus())
                    .paymentStatus(booking.getPaymentStatus())
                    .totalPrice(booking.getTotalPrice())
                    .seatNumber(booking.getSeatNumber())
                    .bookingId(booking.getId())
                    .flight(flightPackageResponse)
                    .user(userResponse)
                    .build();


            ResponseDTO response = AppUtils.getResponseDto("airport records fetched successfully", HttpStatus.OK, bookingResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getBookingBySeatNumber(UUID seatNumber) {
        return null;
    }
}
