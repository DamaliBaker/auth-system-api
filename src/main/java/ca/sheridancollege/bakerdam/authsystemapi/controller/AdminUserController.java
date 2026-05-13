package ca.sheridancollege.bakerdam.authsystemapi.controller;

import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdatePasswordRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdateUserRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.request.UpdateUserRoleRequest;
import ca.sheridancollege.bakerdam.authsystemapi.dto.response.UserResponse;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.mapper.UserMapper;
import ca.sheridancollege.bakerdam.authsystemapi.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserMapper userMapper;
    private final AdminUserService adminService;

    public AdminUserController(UserMapper userMapper,
                               AdminUserService adminService) {
        this.userMapper = userMapper;
        this.adminService = adminService;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        List<UserEntity> users = adminService.getAllUsers();
        List<UserResponse> response = new ArrayList<>();

        for (UserEntity user : users) {
            response.add(userMapper.toResponse(user));
        }

        return response;
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") Long id) {
        return userMapper.toResponse(adminService.findUserById(id));
    }


    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable("id") Long id,
                                   @Valid @RequestBody UpdateUserRequest request) {
        return userMapper.toResponse(adminService.updateUser(id, request.getEmail()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("id") Long id) {
        adminService.deleteUserById(id);
    }

    @PutMapping("/{id}/password")
    public UserResponse resetUserPassword(@PathVariable Long id,
                                           @Valid @RequestBody UpdatePasswordRequest request) {
        return userMapper.toResponse(adminService.resetUserPassword(id, request.getPassword()));
    }

    @PutMapping("/{id}/role")
    public UserResponse updateUserRole(@PathVariable Long id,
                                       @Valid @RequestBody UpdateUserRoleRequest request) {
        return userMapper.toResponse(adminService.updateUserRole(id, request.getRole()));
    }
}
