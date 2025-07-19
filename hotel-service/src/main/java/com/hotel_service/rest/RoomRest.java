package com.hotel_service.rest;

import com.hotel_service.dto.ResponseDTO;
import com.hotel_service.models.Room;
import com.hotel_service.serviceImpl.RoomServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomRest{

    private final RoomServiceImpl roomService;

    @Autowired
    public RoomRest(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        log.info("In fetch all rooms controller->>>");
        return roomService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveRoom(@RequestBody @Valid Room room){
        log.info("In save room record controller->>>");
        return roomService.saveRoom(room);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ResponseDTO> updateRoom(@PathVariable UUID roomId, @RequestBody Room room){
        log.info("In update room record controller->>>");
        return roomService.updateRoom(roomId, room);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ResponseDTO> removeRoom(@PathVariable UUID roomId){
        log.info("In delete room record controller->>>");
        return roomService.removeRoom(roomId);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDTO> getRoomById(@PathVariable UUID roomId){
        log.info("In get room record by id controller->>>");
        return roomService.getRoomById(roomId);
    }
}
