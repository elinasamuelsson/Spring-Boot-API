package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.user.AllUsersDataResponse;
import com.projekt.Spring_Boot_API.responses.user.SingleUserDataResponse;
import com.projekt.Spring_Boot_API.responses.user.LoggedInUserResponse;
import com.projekt.Spring_Boot_API.responses.user.RegisteredUserResponse;
import com.projekt.Spring_Boot_API.requests.user.LoginUserRequest;
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

    @PostMapping("/register")
    public ResponseEntity<RegisteredUserResponse> registerUser(@RequestBody RegisterUserRequest request) {
        RegisteredUserResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoggedInUserResponse> loginUser(@RequestBody LoginUserRequest request) {
        LoggedInUserResponse response = userService.loginUser(request);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {
        userService.updateUser(request);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        userService.deleteUser();

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<AllUsersDataResponse> getAllUsers() {
        AllUsersDataResponse response = userService.getAllUsers();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/get-self")
    public ResponseEntity<SingleUserDataResponse> getOwnUserData() {
        SingleUserDataResponse response = userService.getOwnUserData();

        return ResponseEntity
                .ok()
                .body(response);
    }
}
