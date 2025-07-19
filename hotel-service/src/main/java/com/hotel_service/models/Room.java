package com.hotel_service.models;

import com.hotel_service.config.AuditorData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel_room_tb")
public class Room extends AuditorData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID hotelId;
    private String type;
    private Integer price;
    private Integer capacity;
    private String isBooked;
}
