package com.projekt.Spring_Boot_API.utils;

import com.projekt.Spring_Boot_API.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthenticator {
    public static User authenticateUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
