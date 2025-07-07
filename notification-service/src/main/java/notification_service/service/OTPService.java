package notification_service.service;

import notification_service.dto.OTPPayload;
import notification_service.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface OTPService {
    public void sendOtp(OTPPayload otpPayload);
    public ResponseEntity<ResponseDTO> verifyOtp(OTPPayload otpPayload);
}
