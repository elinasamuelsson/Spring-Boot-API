package com.projekt.Spring_Boot_API.requests.user;

public record LoginUserRequest(
        String username,
        String password
) {
}
