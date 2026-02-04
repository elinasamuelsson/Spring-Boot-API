package com.projekt.Spring_Boot_API.exceptions.folder;

public class OwnerFolderMismatchException extends RuntimeException {
    public OwnerFolderMismatchException() {
        super("User is not the owner of this folder.");
    }
}
