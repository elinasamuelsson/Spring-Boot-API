package com.projekt.Spring_Boot_API.requests;

import com.projekt.Spring_Boot_API.dtos.UpdatedUserDTO;
import com.projekt.Spring_Boot_API.models.User;

public record UpdateUserRequest(
        String username,
        String password
) {
}
