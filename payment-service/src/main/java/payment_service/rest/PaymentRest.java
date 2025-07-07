package payment_service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payment_service.dto.ResponseDTO;
import payment_service.config.kafka.dto.PaymentUpdatePayload;
import payment_service.external.dto.WebHookPayload;
import payment_service.models.Payment;
import payment_service.serviceImpl.PaymentServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentRest {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentRest(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestBody PaymentUpdatePayload paymentUpdatePayload){
        return paymentService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> makePayment(@RequestBody PaymentUpdatePayload paymentUpdatePayload){
        return paymentService.makePayment(paymentUpdatePayload);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<ResponseDTO> updatePayment(@PathVariable UUID paymentId,  @RequestBody Payment paymentPayload){
        return paymentService.updatePayment(paymentId, paymentPayload);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseDTO> getPaymentById(@PathVariable UUID paymentId){
        return paymentService.getPaymentById(paymentId);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ResponseDTO> removePayment(@PathVariable UUID paymentId){
        return paymentService.getPaymentById(paymentId);
    }
    @PostMapping("/webhook")
    public ResponseEntity<Object> getWebhookData(@RequestBody WebHookPayload webHookPayload){
        return paymentService.getWebhookData(webHookPayload);
    }
}
