package com.projekt.Spring_Boot_API.exceptions;

public class PasswordEmptyException extends RuntimeException {
    public PasswordEmptyException() {
        super("Password cannot be empty.");
    }
}
