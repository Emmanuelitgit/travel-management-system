package user_service.serviceImpl;

import user_service.dto.ResponseDTO;
import user_service.models.RoleSetup;
import user_service.models.User;
import user_service.models.UserRole;
import user_service.repo.RoleSetupRepo;
import user_service.repo.UserRepo;
import user_service.repo.UserRoleRepo;
import user_service.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import user_service.util.AppUtils;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepo userRepo;
    private final RoleSetupRepo roleSetupRepo;
    private final UserRoleRepo userRoleRepo;

    @Autowired
    public UserRoleServiceImpl(UserRepo userRepo, RoleSetupRepo roleSetupRepo, UserRoleRepo userRoleRepo) {
        this.userRepo = userRepo;
        this.roleSetupRepo = roleSetupRepo;
        this.userRoleRepo = userRoleRepo;
    }

    @Override
    public ResponseEntity<ResponseDTO> saveUserRole(UUID userId, UUID roleId) {
       try {
           Optional<User> userOptional = userRepo.findById(userId);
           Optional<RoleSetup> roleSetupOptional = roleSetupRepo.findById(roleId);

           if (userOptional.isEmpty() || roleSetupOptional.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("user or role record not found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }

           UserRole userRole = new UserRole();
           userRole.setRoleId(roleId);
           userRole.setUserId(userId);

           UserRole userRoleResponse = userRoleRepo.save(userRole);

           ResponseDTO  response = AppUtils.getResponseDto("users records fetched successfully", HttpStatus.OK, userRoleResponse);
           return new ResponseEntity<>(response, HttpStatus.OK);
       }catch (Exception e) {
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
