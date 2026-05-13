package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.entity.enums.Role;
import ca.sheridancollege.bakerdam.authsystemapi.exception.EmailAlreadyExistsException;
import ca.sheridancollege.bakerdam.authsystemapi.exception.UserNotFoundException;
import ca.sheridancollege.bakerdam.authsystemapi.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void deleteUserById(Long id) {
        UserEntity user = findUserById(id);
        ensureUserIsNotAdmin(user);

        userRepository.delete(user);
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
        UserEntity user = findUserById(id);
        ensureUserIsNotAdmin(user);

        // If the user is changing their email and the email already exists
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        user.setEmail(email);

        return userRepository.save(user);
    }

    @Override
    public UserEntity resetUserPassword(Long id, String password) {
        UserEntity user = findUserById(id);
        ensureUserIsNotAdmin(user);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUserRole(Long id, Role role) {
        UserEntity user = findUserById(id);
        ensureUserIsNotAdmin(user);
        user.setRole(role);

        return userRepository.save(user);
    }

    private void ensureUserIsNotAdmin(UserEntity user) {
        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Admin accounts cannot be modified");
        }
    }
}
