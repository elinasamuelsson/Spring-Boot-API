package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.user.LoggedInUserDTO;
import com.projekt.Spring_Boot_API.dtos.user.RegisteredUserDTO;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.requests.user.LoginUserRequest;
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
        User user = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RegisteredUserDTO.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoggedInUserDTO> loginUser(@RequestBody LoginUserRequest request) {
        String token = userService.loginUser(request);

        return ResponseEntity
                .ok()
                .body(new LoggedInUserDTO(request.username(), token));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {
        userService.updateUser(request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        userService.deleteUser();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @GetMapping("/get-self")
    public ResponseEntity<User> getOwnUserData() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getOwnUserData());
    }
}
