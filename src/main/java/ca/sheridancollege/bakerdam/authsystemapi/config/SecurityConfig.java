package ca.sheridancollege.bakerdam.authsystemapi.config;

import ca.sheridancollege.bakerdam.authsystemapi.dto.response.ErrorResponse;
import ca.sheridancollege.bakerdam.authsystemapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint((request, response, authException) -> {

                                    ErrorResponse error = new ErrorResponse(
                                            LocalDateTime.now(),
                                            HttpStatus.UNAUTHORIZED.value(),
                                            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                            "Authentication is required to access this resource",
                                            request.getRequestURI(),
                                            null
                                    );

                                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                    response.setContentType("application/json");
                                    objectMapper.writeValue(response.getWriter(), error);
                                })
                                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                                    ErrorResponse error = new ErrorResponse(
                                            LocalDateTime.now(),
                                            HttpStatus.FORBIDDEN.value(),
                                            HttpStatus.FORBIDDEN.getReasonPhrase(),
                                            accessDeniedException.getMessage(),
                                            request.getRequestURI(),
                                            null
                                    );

                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    response.setContentType("application/json");
                                    objectMapper.writeValue(response.getWriter(), error);
                                }))
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                .requestMatchers("/api/users/me").authenticated()
                                .requestMatchers("/api/users/me/**").authenticated()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/users").denyAll()
                                .requestMatchers("/api/users/**").denyAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
