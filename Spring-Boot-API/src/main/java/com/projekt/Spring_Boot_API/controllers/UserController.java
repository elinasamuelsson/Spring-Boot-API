package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.user.AllUsersDataResponse;
import com.projekt.Spring_Boot_API.responses.user.SingleUserDataResponse;
import com.projekt.Spring_Boot_API.responses.user.RegisteredUserResponse;
import com.projekt.Spring_Boot_API.requests.user.RegisterUserRequest;
import com.projekt.Spring_Boot_API.requests.user.UpdateUserRequest;
import com.projekt.Spring_Boot_API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegisteredUserResponse> registerUser(@RequestBody RegisterUserRequest request) {
        RegisteredUserResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest request) {
        userService.updateUser(request);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping
    public ResponseEntity<AllUsersDataResponse> getAllUsers() {
        AllUsersDataResponse response = userService.getAllUsers();

        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<SingleUserDataResponse> getOwnUserData() {
        SingleUserDataResponse response = userService.getOwnUserData();

        return ResponseEntity
                .ok()
                .body(response);
    }
}
