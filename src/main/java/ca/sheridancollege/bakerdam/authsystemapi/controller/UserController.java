package ca.sheridancollege.bakerdam.authsystemapi.controller;
import ca.sheridancollege.bakerdam.authsystemapi.entity.UserEntity;
import ca.sheridancollege.bakerdam.authsystemapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserEntity> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserEntity saveUser(@Valid @RequestBody UserEntity user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public UserEntity updateUser(@RequestBody UserEntity user, @PathVariable("id") Long id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "Deleted successfully";
    }
}
