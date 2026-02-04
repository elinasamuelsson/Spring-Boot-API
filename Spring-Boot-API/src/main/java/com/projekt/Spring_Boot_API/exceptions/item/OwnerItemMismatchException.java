package com.projekt.Spring_Boot_API.exceptions.item;

public class OwnerItemMismatchException extends RuntimeException {
    public OwnerItemMismatchException() {
        super("User is not the owner of this item.");
    }
}
