package com.hotel_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private int statusCode;
    private String message;
    private Object data;
    private ZonedDateTime date;
}