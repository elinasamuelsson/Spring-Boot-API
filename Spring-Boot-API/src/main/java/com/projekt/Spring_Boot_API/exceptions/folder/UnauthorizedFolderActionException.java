package com.projekt.Spring_Boot_API.exceptions.folder;

public class UnauthorizedFolderActionException extends RuntimeException {
    public UnauthorizedFolderActionException() {
        super("Root folder cannot be created/modified/deleted.");
    }
}
