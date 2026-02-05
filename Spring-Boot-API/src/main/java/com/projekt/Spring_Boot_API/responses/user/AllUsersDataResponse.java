package com.projekt.Spring_Boot_API.responses.user;

import com.projekt.Spring_Boot_API.models.User;

import java.util.List;

public record AllUsersDataResponse(
        List<SingleUserDataResponse> users
) {
    public static AllUsersDataResponse from(List<User> users) {
        return new AllUsersDataResponse(
                users
                        .stream()
                        .map(SingleUserDataResponse::from)
                        .toList()
        );
    }
}
