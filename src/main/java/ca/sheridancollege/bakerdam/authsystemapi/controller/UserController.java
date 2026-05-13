package ca.sheridancollege.bakerdam.authsystemapi.controller;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.CreateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdatePasswordRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.response.UserResponse;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.mapper.UserMapper;
import ca.sheridancollege.bakerdam.authsystemapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

//    @GetMapping
//    public List<UserResponse> getUsers() {
//        List<UserEntity> users = userService.getAllUsers();
//        List<UserResponse> response = new ArrayList<>();
//
//        for (UserEntity user : users) {
//            response.add(userMapper.toResponse(user));
//        }
//
//        return response;
//    }

//    @GetMapping("/{id}")
//    public UserResponse getUserById(@PathVariable("id") Long id) {
//        return userMapper.toResponse(userService.findUserById(id));
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse saveUser(@Valid @RequestBody CreateUserRequest request) {
        return userMapper.toResponse(userService.saveUser(request));
    }

//    @PutMapping("/{id}")
//    public UserResponse updateUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRequest request) {
//        return userMapper.toResponse(userService.updateUser(id, request.getEmail()));
//    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteUserById(@PathVariable("id") Long id) {
//        userService.deleteUserById(id);
//    }

//    @PutMapping("/{id}/password")
//    public UserResponse updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordRequest request) {
//        return userMapper.toResponse(userService.updatePassword(id, request.getPassword()));
//    }

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

}
