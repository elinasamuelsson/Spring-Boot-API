package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.requests.user.LoginUserRequest;
import com.projekt.Spring_Boot_API.responses.user.LoggedInUserResponse;
import com.projekt.Spring_Boot_API.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/session")
    public ResponseEntity<EntityModel<LoggedInUserResponse>> loginUser(@RequestBody LoginUserRequest request) {
        LoggedInUserResponse response = authService.loginUser(request);

        EntityModel<LoggedInUserResponse> model = EntityModel.of(
                response,
                linkTo(methodOn(UserController.class).getOwnUserData()).withRel("self")
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(model);
    }
}
