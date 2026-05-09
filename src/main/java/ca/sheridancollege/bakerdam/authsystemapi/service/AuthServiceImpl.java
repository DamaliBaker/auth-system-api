package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.dto.response.AuthResponse;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.exception.InvalidCredentialsException;
import ca.sheridancollege.bakerdam.authsystemapi.repository.UserRepository;
import ca.sheridancollege.bakerdam.authsystemapi.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                                        .orElseThrow(InvalidCredentialsException::new);

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new AuthResponse(jwtService.generateToken(user.getEmail()));
        }

        throw new InvalidCredentialsException();
    }
}

