package ca.sheridancollege.bakerdam.authsystemapi.controller;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.CreateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdatePasswordRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.response.UserResponse;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private UserResponse toResponse(UserEntity user) {
        return new UserResponse(user.getId(), user.getEmail());
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        List<UserEntity> users = userService.getAllUsers();
        List<UserResponse> response = new ArrayList<>();

        for (UserEntity user : users) {
            response.add(toResponse(user));
        }

        return response;
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") Long id) {
        return toResponse(userService.findUserById(id));
    }

    @PostMapping
    public UserResponse saveUser(@Valid @RequestBody CreateUserRequest request) {
        return toResponse(userService.saveUser(request));
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRequest request) {
        return toResponse(userService.updateUser(id, request.getEmail()));
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "Deleted successfully";
    }

    @PutMapping("/{id}/password")
    public UserResponse updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordRequest request) {

        return toResponse(userService.updatePassword(id, request.getPassword()));
    }
}
