package com.projekt.Spring_Boot_API.requests.user;

public record RegisterUserRequest(
        String username,
        String password
) {
}
