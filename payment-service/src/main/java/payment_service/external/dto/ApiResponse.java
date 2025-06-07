package payment_service.external.dto;

import lombok.Data;

@Data
public class ApiResponse <T> {
    private String statusCode;
    private String message;
    private T data;
    private String date;
}
