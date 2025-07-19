package com.hotel_service.service;

import com.hotel_service.dto.ResponseDTO;
import com.hotel_service.models.Hotel;
import com.hotel_service.models.Room;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RoomService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> saveRoom(Room room);
    ResponseEntity<ResponseDTO> updateRoom (UUID roomId, Room room);
    ResponseEntity<ResponseDTO> removeRoom (UUID roomId);
    ResponseEntity<ResponseDTO> getRoomById(UUID roomId);
}
