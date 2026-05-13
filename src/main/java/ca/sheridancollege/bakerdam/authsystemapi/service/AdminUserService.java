package ca.sheridancollege.bakerdam.authsystemapi.service;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.entity.enums.Role;

import java.util.List;

public interface AdminUserService {
    void deleteUserById(Long id);
    UserEntity findUserById(Long id);
    List<UserEntity> getAllUsers();
    UserEntity updateUser(Long id, String email);
    UserEntity resetUserPassword(Long id, String password);
    UserEntity updateUserRole(Long id, Role role);
}
