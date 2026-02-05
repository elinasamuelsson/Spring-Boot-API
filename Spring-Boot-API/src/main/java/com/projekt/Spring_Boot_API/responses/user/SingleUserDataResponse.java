package com.projekt.Spring_Boot_API.responses.user;

import com.projekt.Spring_Boot_API.models.User;

import java.util.UUID;

public record SingleUserDataResponse(
        UUID userId,
        String username
) {
    public static SingleUserDataResponse from(User user) {
        return new SingleUserDataResponse(
                user.getUserId(),
                user.getUsername()
        );
    }
}
