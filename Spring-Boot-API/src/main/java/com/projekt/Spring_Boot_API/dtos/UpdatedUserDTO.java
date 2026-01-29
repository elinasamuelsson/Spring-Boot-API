package com.projekt.Spring_Boot_API.dtos;

import com.projekt.Spring_Boot_API.models.User;

import java.util.UUID;

public record UpdatedUserDTO(
        UUID userId,
        String username
) {
    public static UpdatedUserDTO from(User user) {
        return new UpdatedUserDTO(
                user.getUserId(),
                user.getUsername()
        );
    }
}
