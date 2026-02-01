package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.user.RegisteredUserDTO;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.requests.user.RegisterUserRequest;
import com.projekt.Spring_Boot_API.requests.user.UpdateUserRequest;
import com.projekt.Spring_Boot_API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisteredUserDTO> registerUser(@RequestBody RegisterUserRequest request) {
        User user = userService.registerUser(request.username(), request.password());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RegisteredUserDTO.from(user));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        userService.updateUser(userId, request.username(), request.password());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/all") //TODO: Add DTO response entity to manage what shows up
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @GetMapping("/{userId}") //TODO: Add DTO response entity to manage what shows up
    public ResponseEntity<User> getUser(@PathVariable UUID userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserById(userId));
    }
}
