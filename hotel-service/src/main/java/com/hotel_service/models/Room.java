package com.hotel_service.models;

import com.hotel_service.config.AuditorData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel_room_tb")
public class Room extends AuditorData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "hotel id cannot be null")
    private UUID hotelId;
    @NotNull(message = "type cannot be null")
    private String type;
    @NotNull(message = "price cannot be null")
    private Float price;
    @NotNull(message = "capacity cannot be null")
    private Integer capacity;
    @NotNull(message = "quantity cannot be null")
    private Integer quantity;
    private Boolean isBooked;
}
