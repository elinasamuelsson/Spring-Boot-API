package com.projekt.Spring_Boot_API.responses.item;

import com.projekt.Spring_Boot_API.models.Item;

import java.util.UUID;

public record UploadedItemResponse(
        String itemName,
        int itemSizeBytes,
        UUID itemLocation,
        UUID itemOwner
) {
    public static UploadedItemResponse from(Item item) {
        return new UploadedItemResponse(
                item.getItemName(),
                item.getItemSizeBytes(),
                item.getFolder().getFolderId(),
                item.getUser().getUserId()
        );
    }
}
