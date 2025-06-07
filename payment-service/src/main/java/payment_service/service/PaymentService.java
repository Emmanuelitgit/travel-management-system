package payment_service.service;

import org.springframework.http.ResponseEntity;
import payment_service.dto.ResponseDTO;
import payment_service.external.dto.PaymentPayload;
import payment_service.external.dto.WebHookPayload;
import payment_service.models.Payment;

import java.util.UUID;

public interface PaymentService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> makePayment(PaymentPayload payment);
    ResponseEntity<ResponseDTO> updatePayment(UUID paymentId, Payment payment);
    ResponseEntity<ResponseDTO> removePayment(UUID paymentId);
    ResponseEntity<ResponseDTO> getPaymentById(UUID paymentId);
    ResponseEntity getWebhookData(WebHookPayload webHookPayload);
}
