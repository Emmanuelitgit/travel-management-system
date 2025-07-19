package com.hotel_service.repo;

import com.hotel_service.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomRepo extends JpaRepository<Room, UUID> {
}
