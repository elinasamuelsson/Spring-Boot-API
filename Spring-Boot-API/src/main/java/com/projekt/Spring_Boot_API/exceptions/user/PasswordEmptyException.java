package com.projekt.Spring_Boot_API.exceptions.user;

public class PasswordEmptyException extends RuntimeException {
    public PasswordEmptyException() {
        super("Password cannot be empty.");
    }
}
