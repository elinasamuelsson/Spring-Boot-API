package com.projekt.Spring_Boot_API.exceptions.user;

public class UnauthorizedUserActionException extends RuntimeException {
    public UnauthorizedUserActionException() {
        super("Cannot edit or delete other users.");
    }
}
