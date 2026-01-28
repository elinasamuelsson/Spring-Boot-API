package com.projekt.Spring_Boot_API.exceptions;

public class UsernameEmptyException extends RuntimeException {
    public UsernameEmptyException() {
        super("Username cannot be empty.");
    }
}
