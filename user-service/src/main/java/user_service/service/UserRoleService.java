package user_service.service;

import org.springframework.http.ResponseEntity;
import user_service.dto.ResponseDTO;

import java.util.UUID;

public interface UserRoleService {
    ResponseEntity<user_service.dto.ResponseDTO> saveUserRole(UUID userId, UUID roleId);
}
