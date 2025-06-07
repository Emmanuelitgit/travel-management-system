package payment_service.serviceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import payment_service.dto.ResponseDTO;
import payment_service.models.Payment;
import payment_service.service.PaymentService;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> makePayment(Payment payment) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updatePayment(UUID paymentId, Payment payment) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removePayment(UUID paymentId) {
        return null;
    }
}
