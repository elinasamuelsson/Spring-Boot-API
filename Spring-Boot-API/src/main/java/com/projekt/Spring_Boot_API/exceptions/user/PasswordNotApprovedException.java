package com.projekt.Spring_Boot_API.exceptions.user;

public class PasswordNotApprovedException extends RuntimeException {
    public PasswordNotApprovedException() {
        super("This password is not approved. Passwords needs to contain at least one number and one special character, and be longer than 8 characters.");
    }
}
