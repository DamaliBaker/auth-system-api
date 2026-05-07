package ca.sheridancollege.bakerdam.authsystemapi.controller;

import ca.sheridancollege.bakerdam.authsystemapi.dto.request.LoginRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.response.AuthResponse;
import ca.sheridancollege.bakerdam.authsystemapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

}
