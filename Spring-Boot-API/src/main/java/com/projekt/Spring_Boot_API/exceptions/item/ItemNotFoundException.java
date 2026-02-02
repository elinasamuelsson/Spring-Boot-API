package com.projekt.Spring_Boot_API.exceptions.item;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
        super("Item not found.");
    }
}
