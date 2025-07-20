package com.hotel_service.rest;

import com.hotel_service.dto.ResponseDTO;
import com.hotel_service.models.Hotel;
import com.hotel_service.serviceImpl.HotelServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/ap/v1/hotels")
public class HotelRest {

    private final HotelServiceImpl hotelService;

    @Autowired
    public HotelRest(HotelServiceImpl hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        log.info("In fetch hotel records controller->>>");
        return hotelService.findAll();
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveHotel(@RequestBody @Valid Hotel hotel){
        log.info("In save hotel record controller->>>{}", hotel);
        return hotelService.saveHotel(hotel);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<ResponseDTO> updateHotel(@PathVariable UUID hotelId, @RequestBody @Valid Hotel hotel){
        log.info("In update hotel record controller->>>{}", hotel);
        return hotelService.updateHotel(hotelId, hotel);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<ResponseDTO> removeHotel(@PathVariable UUID hotelId){
        log.info("In remove hotel record controller->>>Id:->>>{}", hotelId);
        return hotelService.removeHotel(hotelId);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ResponseDTO> getHotelById(@PathVariable UUID hotelId){
        log.info("In get hotel record by id controller->>>d:->>>{}", hotelId);
        return hotelService.getHotelById(hotelId);
    }

    @GetMapping("/location")
    public ResponseEntity<ResponseDTO> getHotelLocation(@RequestParam String query){
        log.info("In get hotel location controller->>>");
        return hotelService.getHotelLocation(query);
    }
}