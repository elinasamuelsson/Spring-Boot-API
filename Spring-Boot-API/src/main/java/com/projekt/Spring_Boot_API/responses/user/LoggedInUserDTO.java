package com.projekt.Spring_Boot_API.responses.user;

public record LoggedInUserDTO(
        String username,
        String token
) {
    public LoggedInUserDTO (String username, String token) {
        this.username = username;
        this.token = token;
    }
}
