package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.user.AllUsersDataResponse;
import com.projekt.Spring_Boot_API.responses.user.SingleUserDataResponse;
import com.projekt.Spring_Boot_API.responses.user.RegisteredUserResponse;
import com.projekt.Spring_Boot_API.requests.user.RegisterUserRequest;
import com.projekt.Spring_Boot_API.requests.user.UpdateUserRequest;
import com.projekt.Spring_Boot_API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<EntityModel<RegisteredUserResponse>> registerUser(@RequestBody RegisterUserRequest request) {
        RegisteredUserResponse response = userService.registerUser(request);

        EntityModel<RegisteredUserResponse> model = EntityModel.of(
                response,
                linkTo(methodOn(AuthController.class).loginUser(null)).withRel("login")
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(model);
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
    public ResponseEntity<EntityModel<SingleUserDataResponse>> getOwnUserData() {
        SingleUserDataResponse response = userService.getOwnUserData();

        EntityModel<SingleUserDataResponse> model = EntityModel.of(
                response,
                linkTo(methodOn(FolderController.class).getContents(response.rootFolder().getFolderId())).withRel("rootFolder"),
                linkTo(methodOn(UserController.class).getOwnUserData()).withRel("update"),
                linkTo(methodOn(UserController.class).getOwnUserData()).withRel("delete")
        );

        return ResponseEntity
                .ok()
                .body(model);
    }
}
