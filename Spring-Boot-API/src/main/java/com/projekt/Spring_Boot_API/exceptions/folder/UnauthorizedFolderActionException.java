package com.projekt.Spring_Boot_API.exceptions.folder;

public class UnauthorizedFolderActionException extends RuntimeException {
    public UnauthorizedFolderActionException(String message) {
        super(message);
    }
}
