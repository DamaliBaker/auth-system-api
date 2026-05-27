package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.dto.response.AuthResponse;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.exception.InvalidCredentialsException;
import ca.sheridancollege.bakerdam.authsystemapi.exception.TooManyLoginAttemptsException;
import ca.sheridancollege.bakerdam.authsystemapi.repository.UserRepository;
import ca.sheridancollege.bakerdam.authsystemapi.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public AuthResponse login(String email, String password, String ipAddress) {
        if (loginAttemptService.isBlocked(email, ipAddress)) {
            throw new TooManyLoginAttemptsException();
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    loginAttemptService.recordFailedAttempt(email, ipAddress);
                    return new InvalidCredentialsException();
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            loginAttemptService.recordFailedAttempt(email, ipAddress);
            throw new InvalidCredentialsException();
        }

        loginAttemptService.clearEmailAttempts(email);

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }
}

