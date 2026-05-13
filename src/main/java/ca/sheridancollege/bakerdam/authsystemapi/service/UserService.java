package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.dto.request.CreateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;

public interface UserService {
    UserEntity saveUser(CreateUserRequest request);
    UserEntity getCurrentUser();
    UserEntity updateCurrentUser(String email);
    UserEntity updateCurrentUserPassword(String password);
    void deleteCurrentUser();
}
