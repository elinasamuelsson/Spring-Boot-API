package com.projekt.Spring_Boot_API.dtos.user;

import com.projekt.Spring_Boot_API.models.User;

public record LoggedInUserDTO(
        String username,
        String token
) {
    public LoggedInUserDTO (String username, String token) {
        this.username = username;
        this.token = token;
    }
}
