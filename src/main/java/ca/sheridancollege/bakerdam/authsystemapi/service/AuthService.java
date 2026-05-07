package ca.sheridancollege.bakerdam.authsystemapi.service;

import ca.sheridancollege.bakerdam.authsystemapi.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String email, String password);
}
