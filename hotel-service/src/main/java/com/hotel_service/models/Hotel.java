package com.hotel_service.models;

import com.hotel_service.config.AuditorData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel_tb")
public class Hotel extends AuditorData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "hotel name cannot be null")
    private String name;
    @NotNull(message = "city cannot be null")
    private String city;
    @NotNull(message = "location cannot be null")
    private String location;
    @NotNull(message = "country cannot be null")
    private String country;
    private String amenities;
    @NotNull(message = "provider id cannot be null")
    private UUID providerId;
}
