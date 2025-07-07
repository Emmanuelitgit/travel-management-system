package user_service.service;

import user_service.dto.ResponseDTO;
import user_service.models.RoleSetup;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RoleSetupService {

    ResponseEntity<ResponseDTO> saveRole(RoleSetup roleSetup);
    ResponseEntity<ResponseDTO> updateRole(RoleSetup roleSetup, UUID roleId);
    ResponseEntity<ResponseDTO> findRoleById(UUID roleId);
    ResponseEntity<ResponseDTO> deleteRole(UUID roleId);
    ResponseEntity<ResponseDTO> getRoles();
}
