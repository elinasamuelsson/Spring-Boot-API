package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.RegisteredUserDTO;
import com.projekt.Spring_Boot_API.dtos.UpdatedUserDTO;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.requests.RegisterUserRequest;
import com.projekt.Spring_Boot_API.requests.UpdateUserRequest;
import com.projekt.Spring_Boot_API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register/user")
    public ResponseEntity<RegisteredUserDTO> registerUser(@RequestBody RegisterUserRequest request) {
        User user = userService.registerUser(request.username(), request.password());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RegisteredUserDTO.from(user));
    }

    @PutMapping("/update/user/{userId}")
    public ResponseEntity<UpdatedUserDTO> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        User user = userService.updateUser(userId, request.username(), request.password());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UpdatedUserDTO.from(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserById(userId));
    }
}
