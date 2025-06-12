package payment_service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payment_service.config.kafka.dto.BookingUpdatePayload;
import payment_service.config.kafka.dto.PaymentAuthorizationPayload;
import payment_service.config.kafka.dto.PaymentUpdatePayload;
import payment_service.dto.ResponseDTO;
import payment_service.exception.BadRequestException;
import payment_service.exception.NotFoundException;
import payment_service.exception.ServerException;
import payment_service.external.ServiceCalls;
import payment_service.external.dto.*;
import payment_service.models.Payment;
import payment_service.repo.PaymentRepo;
import payment_service.service.PaymentService;
import payment_service.util.AppUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final ServiceCalls serviceCalls;
    private final PaymentRepo paymentRepo;
    private final KafkaTemplate<String, PaymentAuthorizationPayload> paymentNotificationPkafkaTemplate;
    private final KafkaTemplate<String, BookingUpdatePayload> bookingUpdateKafkaTemplate;

    @Autowired
    public PaymentServiceImpl(ServiceCalls serviceCalls, PaymentRepo paymentRepo, KafkaTemplate<String, PaymentAuthorizationPayload> paymentNotificationPkafkaTemplate, KafkaTemplate<String, BookingUpdatePayload> bookingUpdateKafkaTemplate) {
        this.serviceCalls = serviceCalls;
        this.paymentRepo = paymentRepo;
        this.paymentNotificationPkafkaTemplate = paymentNotificationPkafkaTemplate;
        this.bookingUpdateKafkaTemplate = bookingUpdateKafkaTemplate;
    }

    /**
     * @description Fetches all payments records from the database.
     * @return ResponseEntity containing a list of payments records and status information.
     * @author Emmanuel Yidana
     * @createdAt 7th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
       try{
           log.info("in fetch all payments records");
           List<Payment> payments = paymentRepo.findAll();
           if (payments.isEmpty()){
               throw new NotFoundException("no payment record found");
           }
           ResponseDTO responseDTO = AppUtils.getResponseDto("payment records", HttpStatus.OK, payments);
           return new ResponseEntity<>(responseDTO, HttpStatus.OK);
       } catch (NotFoundException e) {
           throw new NotFoundException(e.getMessage());
       }catch (ServerException e) {
           throw new ServerException(e.getMessage());
       }
    }

    /**
     * @description Saves a new payment record to the database.
     * @param paymentUpdatePayload the payment record to save.
     * @return ResponseEntity containing the saved payment record and status info.
     * @author Emmanuel Yidana
     * @createdAt 7th, June 2025
     */
    @KafkaListener(topics = "hey", containerFactory = "paymentUpdateKafkaListenerContainerFactory", groupId = "payment-group")
    @Override
    public ResponseEntity<ResponseDTO> makePayment(PaymentUpdatePayload paymentUpdatePayload) {

        log.info("About to initiate payment process:->>>>>");
//        BookingResponse bookingResponse = serviceCalls.updateBooking(payment.getBookingId(), bookingUpdatePayload).block();

        // make a call to payStack to initiate payment process
        log.info("About to instantiate payment process:->>>>{}", paymentUpdatePayload.getAmount());
        PaymentResponse paymentResponse = serviceCalls.makePayment(paymentUpdatePayload).block();
        assert paymentResponse != null;
        log.info("Payment response:->>>{}", paymentResponse);

        // publish an update to notification service to send payment authorization to user
        log.info("About to publish an update to notification service:->>>>");
        PaymentAuthorizationPayload paymentAuthorizationPayload = PaymentAuthorizationPayload
                .builder()
                .authorization_url(paymentResponse.getData().getAuthorization_url())
                .email(paymentUpdatePayload.getEmail())
                .build();
        paymentNotificationPkafkaTemplate.send("paymentNotification", paymentAuthorizationPayload);

        // publish an update to booking service to update booking status
        log.info("About to publish an update to booking service:->>>>{} {}", PaymentStatus.PROCESSING, BookingStatus.AWAITING_PAYMENT);
        BookingUpdatePayload bookingUpdatePayload = BookingUpdatePayload
                .builder()
                .id(paymentUpdatePayload.getBookingId())
                .paymentStatus(PaymentStatus.PROCESSING.toString())
                .bookingStatus(BookingStatus.AWAITING_PAYMENT.toString())
                .build();
        bookingUpdateKafkaTemplate.send("bookingUpdate", bookingUpdatePayload);

        log.info("Payment process initiated successfully:->>>>");
        ResponseDTO responseDTO = AppUtils.getResponseDto("payment success", HttpStatus.OK);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * @description Updates an existing booking record identified by ID.
     * @param paymentId the ID of the booking to update.
     * @param payment the updated payment data.
     * @return ResponseEntity containing the updated payment record and status info.
     * @author Emmanuel Yidana
     * @createdAt 7th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updatePayment(UUID paymentId, Payment payment) {
        try{
            Payment existingData = paymentRepo.findById(paymentId)
                    .orElseThrow(()-> new NotFoundException("no payment record found"));

            if(
                    payment.getPaymentStatus().equalsIgnoreCase(PaymentStatus.PAID.toString()) ||
                    payment.getPaymentStatus().equalsIgnoreCase(PaymentStatus.PROCESSING.toString()) ||
                    payment.getPaymentStatus().equalsIgnoreCase(PaymentStatus.PENDING.toString()) ||
                    payment.getPaymentStatus().equalsIgnoreCase(PaymentStatus.CANCELLED.toString())
            ){
                existingData.setPaymentStatus(payment.getPaymentStatus() !=null? payment.getPaymentStatus().toUpperCase() : existingData.getPaymentStatus());
            }else {
                throw new BadRequestException("unknown payment status");
            }

            Payment paymentResponse = paymentRepo.save(existingData);

            ResponseDTO responseDTO = AppUtils.getResponseDto("payment record updated", HttpStatus.OK, paymentResponse);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }catch (ServerException e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Deletes a payment record based on the given ID.
     * @param paymentId the ID of the payment to delete.
     * @return ResponseEntity indicating whether the deletion was successful.
     * @author Emmanuel Yidana
     * @createdAt 7th, June 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removePayment(UUID paymentId) {
        try{
            Payment payment = paymentRepo.findById(paymentId)
                    .orElseThrow(()-> new NotFoundException("no payment record found"));
            paymentRepo.deleteById(payment.getId());

            ResponseDTO responseDTO = AppUtils.getResponseDto("payment record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ServerException e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description Fetches a specific payment record by ID.
     * @param paymentId the ID of the payment record to retrieve.
     * @return ResponseEntity containing the payment record and status info.
     * @author Emmanuel Yidana
     * @createdAt 7th, June 2025
     * */
    @Override
    public ResponseEntity<ResponseDTO> getPaymentById(UUID paymentId) {
        try{
            Payment payment = paymentRepo.findById(paymentId)
                    .orElseThrow(()-> new NotFoundException("no payment record found"));

            ResponseDTO responseDTO = AppUtils.getResponseDto("payment record fetched successfully", HttpStatus.OK, payment);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ServerException e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description This method is used to listen to updates from payStack.
     * @param webHookPayload the object containing the data to be received from payStack.
     * @return ResponseEntity containing the status code of the operation which will serve as acknowledgement of receiving the data.
     * @author Emmanuel Yidana
     * @createdAt 7th, June 2025
     * */
    @Override
    public ResponseEntity<Object> getWebhookData(WebHookPayload webHookPayload) {
        if (webHookPayload != null){
            WebHookPayload.Data data = webHookPayload.getData();
            // load payment record by reference number
            Optional<Payment> payment = paymentRepo.findByReference(data.getReference());
            if (payment.isPresent() && data.getStatus().equalsIgnoreCase("success")){
                Payment existingData = payment.get();

                // prepare data to update booking status
                BookingUpdatePayload bookingUpdatePayload = BookingUpdatePayload
                        .builder()
                        .id(payment.get().getBookingId())
                        .bookingStatus(BookingStatus.CONFIRMED.toString())
                        .paymentStatus(PaymentStatus.PAID.toString())
                        .build();
                // make call to booking service to update booking status
//                serviceCalls.updateBooking(existingData.getBookingId(), bookingUpdatePayload).block();

                // publish an update to booking service to update booking status
                bookingUpdateKafkaTemplate.send("bookingUpdate", bookingUpdatePayload);

                // save updated payment records
                existingData.setPaymentStatus(PaymentStatus.PAID.toString());
                existingData.setTransactionId(data.getId());
                existingData.setCurrency(data.getCurrency());
                existingData.setChannel(data.getChannel());
                existingData.setPaymentDate(data.getPaid_at());
                paymentRepo.save(existingData);

                return new ResponseEntity<>(HttpStatus.OK);
            }

        }
        return null;
    }
}