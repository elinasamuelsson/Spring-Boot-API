package com.projekt.Spring_Boot_API.exceptions.folder;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException() {
        super("Folder not found.");
    }
}
