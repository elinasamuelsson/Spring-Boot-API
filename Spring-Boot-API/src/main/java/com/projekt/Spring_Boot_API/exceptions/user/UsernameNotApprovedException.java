package com.projekt.Spring_Boot_API.exceptions.user;

public class UsernameNotApprovedException extends RuntimeException {
    public UsernameNotApprovedException() {
        super("This username is unavailable or too short. Usernames need to be longer than 5 characters.");
    }
}
