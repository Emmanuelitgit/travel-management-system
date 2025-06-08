package notification_service.exception;
import io.jsonwebtoken.ExpiredJwtException;
import notification_service.dto.ResponseDTO;
import notification_service.util.AppUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UnAuthorizeException.class)
    ResponseEntity<ResponseDTO> handleUnAuthorizeException(UnAuthorizeException exception){
        ResponseDTO response = AppUtils.getResponseDto(exception.getMessage(), HttpStatus.valueOf(401));
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<ResponseDTO> handleExpiredJwtException(){
        ResponseDTO response = AppUtils.getResponseDto("Invalid token", HttpStatus.valueOf(401));
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleNotFoundException(NotFoundException exception){
        ResponseDTO response = AppUtils.getResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ServerException.class)
    ResponseEntity<Object> handleServerException(ServerException exception){
        ResponseDTO response = AppUtils.getResponseDto(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    ResponseEntity<Object> handleBadRequestException(BadRequestException exception){
        ResponseDTO response = AppUtils.getResponseDto(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, Object> res = new HashMap<>();

        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        res.put("message", errors);
        res.put("status", HttpStatusCode.valueOf(400));
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(400));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AlreadyExistException.class)
    ResponseEntity<Object> handleAlreadyExistException(AlreadyExistException exception){
        ResponseDTO response = AppUtils.getResponseDto(exception.getMessage(), HttpStatus.ALREADY_REPORTED);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(208));
    }
}
