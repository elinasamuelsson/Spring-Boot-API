package com.projekt.Spring_Boot_API.requests.user;

public record UpdateUserRequest(
        String username,
        String password
) {
}
