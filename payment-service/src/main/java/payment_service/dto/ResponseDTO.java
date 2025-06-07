package payment_service.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Data
public class ResponseDTO {
    private int statusCode;
    private String message;
    private Object data;
    private ZonedDateTime date;
}