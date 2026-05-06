package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.repository.UserRepository;
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
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash PW
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity updateUser(Long id, String email) { // Updates email only
        UserEntity existingUser = userRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(email);

        return userRepository.save(existingUser);
    }

    @Override
    public UserEntity updatePassword(Long id, String password) {
        UserEntity user = userRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}
