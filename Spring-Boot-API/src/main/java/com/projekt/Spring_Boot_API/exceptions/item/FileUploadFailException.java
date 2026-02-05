package com.projekt.Spring_Boot_API.exceptions.item;

public class FileUploadFailException extends RuntimeException {
    public FileUploadFailException() {
        super("Failed to upload file.");
    }
}
