package com.projekt.Spring_Boot_API.exceptions.folder;

public class FolderNameEmptyException extends RuntimeException {
    public FolderNameEmptyException() {
        super("Folder name cannot be empty.");
    }
}
