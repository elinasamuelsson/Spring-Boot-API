package com.projekt.Spring_Boot_API.responses.user;

import com.projekt.Spring_Boot_API.models.User;

import java.util.UUID;

public record RegisteredUserResponse(
        UUID userId,
        String username
) {
    public static RegisteredUserResponse from(User user) {
        return new RegisteredUserResponse(user.getUserId(), user.getUsername());
    }
}
