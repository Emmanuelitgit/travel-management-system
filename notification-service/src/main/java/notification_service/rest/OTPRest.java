package notification_service.rest;

import jakarta.validation.Valid;
import notification_service.dto.OTPPayload;
import notification_service.dto.ResponseDTO;
import notification_service.serviceImpl.OTPServiceImpl;
import notification_service.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
public class OTPRest {

    private final OTPServiceImpl otpService;

    @Autowired
    public OTPRest(OTPServiceImpl otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<ResponseDTO> sendOTP(@RequestBody OTPPayload otpPayload){
        otpService.sendOtp(otpPayload);
        ResponseDTO response = AppUtils.getResponseDto("OTP sent", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseDTO> verifyOTP(@RequestBody @Valid OTPPayload otpPayload){
        return otpService.verifyOtp(otpPayload);
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseDTO> testKafka(){
        otpService.testKafka();
        ResponseDTO response = AppUtils.getResponseDto("OTP sent", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}