package com.projekt.Spring_Boot_API.requests;

public record RegisterUserRequest(
        String username,
        String password
) {
}
