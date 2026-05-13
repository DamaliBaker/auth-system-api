package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.dto.request.CreateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.exception.EmailAlreadyExistsException;
import ca.sheridancollege.bakerdam.authsystemapi.exception.InvalidCredentialsException;
import ca.sheridancollege.bakerdam.authsystemapi.exception.UserNotFoundException;
import ca.sheridancollege.bakerdam.authsystemapi.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {

        UserEntity currentUser = getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You can only update your own account");
        }

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity updateUser(Long id, String email) { // Updates email only

        UserEntity currentUser = getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You can only update your own account");
        }

        // If the user is changing their email and the email already exists
        if (!currentUser.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        currentUser.setEmail(email);

        return userRepository.save(currentUser);
    }

    @Override
    public UserEntity updatePassword(Long id, String password) {
        UserEntity currentUser = getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You can only update your own account");
        }

        currentUser.setPassword(passwordEncoder.encode(password));

        return userRepository.save(currentUser);
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
}
