package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.dto.request.CreateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.entity.enums.Role;
import ca.sheridancollege.bakerdam.authsystemapi.exception.EmailAlreadyExistsException;
import ca.sheridancollege.bakerdam.authsystemapi.exception.InvalidCredentialsException;
import ca.sheridancollege.bakerdam.authsystemapi.exception.UserNotFoundException;
import ca.sheridancollege.bakerdam.authsystemapi.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity saveUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
    }

    @Override
    public void deleteCurrentUser() {
        UserEntity user = getCurrentUser();

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Admin accounts cannot be deleted through self-service");
        }

        userRepository.delete(user);
    }

    @Override
    public UserEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new InvalidCredentialsException();
        }

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public UserEntity updateCurrentUser(String email) {
        UserEntity user = getCurrentUser();

        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        user.setEmail(email);

        return userRepository.save(user);
    }

    @Override
    public UserEntity updateCurrentUserPassword(String password) {
        UserEntity user = getCurrentUser();

        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}
