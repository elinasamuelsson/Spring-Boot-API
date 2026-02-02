package com.projekt.Spring_Boot_API.dtos.item;

import com.projekt.Spring_Boot_API.models.Item;

import java.util.UUID;

public record UploadedItemDTO(
        String itemName,
        int itemSizeBytes,
        UUID itemLocation,
        UUID itemOwner
) {
    public static UploadedItemDTO from(Item item) {
        return new UploadedItemDTO(
                item.getItemName(),
                item.getItemSizeBytes(),
                item.getFolder().getFolderId(),
                item.getUser().getUserId()
        );
    }
}
