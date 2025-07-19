package com.hotel_service.service;

import com.hotel_service.dto.ResponseDTO;
import com.hotel_service.models.Hotel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RoomService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveHotel(Hotel hotel);
    ResponseEntity<ResponseDTO> updateHotel(UUID hotelId, Hotel hotel);
    ResponseEntity<ResponseDTO> removeHotel(UUID hotelId);
    ResponseEntity<ResponseDTO> getHotelById(UUID hotelId);
}
