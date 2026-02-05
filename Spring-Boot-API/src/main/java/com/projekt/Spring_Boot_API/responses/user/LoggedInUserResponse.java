package com.projekt.Spring_Boot_API.responses.user;

import java.util.HashMap;

public record LoggedInUserResponse(
        String username,
        String token
) {
    public static LoggedInUserResponse from(HashMap<String, String> credentials) {
        return new LoggedInUserResponse(
                credentials.get("username"),
                credentials.get("token"));
    }
}
