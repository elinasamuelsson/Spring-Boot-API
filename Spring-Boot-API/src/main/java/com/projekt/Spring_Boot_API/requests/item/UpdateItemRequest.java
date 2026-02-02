package com.projekt.Spring_Boot_API.requests.item;

import java.util.UUID;

public record UpdateItemRequest(
        String itemName,
        UUID itemLocationId
) {
}
