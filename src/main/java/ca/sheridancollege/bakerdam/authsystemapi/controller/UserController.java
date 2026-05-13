package ca.sheridancollege.bakerdam.authsystemapi.controller;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.CreateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdatePasswordRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.response.UserResponse;
import ca.sheridancollege.bakerdam.authsystemapi.mapper.UserMapper;
import ca.sheridancollege.bakerdam.authsystemapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse saveUser(@Valid @RequestBody CreateUserRequest request) {
        return userMapper.toResponse(userService.saveUser(request));
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return userMapper.toResponse(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public UserResponse updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        return userMapper.toResponse(userService.updateCurrentUser(request.getEmail()));
    }

    @PutMapping("/me/password")
    public UserResponse updateCurrentUserPassword(@Valid @RequestBody UpdatePasswordRequest request) {
        return userMapper.toResponse(userService.updateCurrentUserPassword(request.getPassword()));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser() {
        userService.deleteCurrentUser();
    }
}
