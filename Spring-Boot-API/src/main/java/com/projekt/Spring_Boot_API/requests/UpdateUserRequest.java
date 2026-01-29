package com.projekt.Spring_Boot_API.requests;

public record UpdateUserRequest(
        String username,
        String password
) {
}
