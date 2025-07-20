package com.hotel_service.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelLocationResponse {
    private String name;
    private String display_name;
    private double lat;
    private double lon;

    private String houseNumber;
    private String road;
    private String suburb;
    private String city;
    private String county;
    private String state;
    private String country;
    private String countryCode;
}
