package com.projekt.Spring_Boot_API.responses.item;

import com.projekt.Spring_Boot_API.models.Item;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
public class SingleItemResponse extends RepresentationModel<SingleItemResponse> {
    private final UUID itemId;
    private final String itemName;
    private final int itemSize;
    private final UUID itemLocation;

    public SingleItemResponse(UUID itemId, String itemName, int itemSize, UUID itemLocation) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemSize = itemSize;
        this.itemLocation = itemLocation;
    }

    public static SingleItemResponse from(Item item) {
        return new SingleItemResponse(
                item.getItemId(),
                item.getItemName(),
                item.getItemSizeBytes(),
                item.getFolder().getFolderId()
        );
    }
}
