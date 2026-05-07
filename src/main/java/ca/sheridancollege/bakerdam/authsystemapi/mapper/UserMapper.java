package ca.sheridancollege.bakerdam.authsystemapi.mapper;

import ca.sheridancollege.bakerdam.authsystemapi.dto.response.UserResponse;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(UserEntity user) {
        return new UserResponse(user.getId(), user.getEmail());
    }

}
