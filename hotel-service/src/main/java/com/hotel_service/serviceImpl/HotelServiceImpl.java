package com.hotel_service.serviceImpl;

import com.hotel_service.dto.ResponseDTO;
import com.hotel_service.models.Hotel;
import com.hotel_service.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> saveHotel(Hotel hotel) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateHotel(UUID hotelId, Hotel hotel) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removeHotel(UUID hotelId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getHotelById(UUID hotelId) {
        return null;
    }
}
