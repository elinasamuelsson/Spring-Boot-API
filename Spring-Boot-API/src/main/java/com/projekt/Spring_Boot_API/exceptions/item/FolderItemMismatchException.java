package com.projekt.Spring_Boot_API.exceptions.item;

public class FolderItemMismatchException extends RuntimeException {
    public FolderItemMismatchException() {
        super("Item is not located in this folder.");
    }
}
