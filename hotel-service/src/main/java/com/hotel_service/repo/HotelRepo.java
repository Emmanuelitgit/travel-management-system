package com.hotel_service.repo;

import com.hotel_service.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, UUID> {
}
