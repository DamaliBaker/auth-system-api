package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.dto.request.CreateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(CreateUserRequest request);
    void deleteUserById(Long id);
    UserEntity findUserById(Long id);
    List<UserEntity> getAllUsers();
    UserEntity updateUser(Long id, String email);
    UserEntity updatePassword(Long id, String password);
    UserEntity getCurrentUser();
}
