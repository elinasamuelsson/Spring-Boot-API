package com.projekt.Spring_Boot_API.responses.item;

import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.models.User;

import java.util.UUID;

public record SingleItemResponse(
        UUID itemId,
        String itemName,
        int itemSize,
        UUID itemLocation
) {
    public static SingleItemResponse from(Item item) {
        return new SingleItemResponse(
                item.getItemId(),
                item.getItemName(),
                item.getItemSizeBytes(),
                item.getFolder().getFolderId()
        );
    }
}
