package com.projekt.Spring_Boot_API.dtos.user;

import com.projekt.Spring_Boot_API.models.User;

import java.util.UUID;

public record RegisteredUserDTO(
        UUID userId,
        String username
) {
    public static RegisteredUserDTO from(User user) {
        return new RegisteredUserDTO(
                user.getUserId(),
                user.getUsername()
        );
    }
}
