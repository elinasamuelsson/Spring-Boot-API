package com.projekt.Spring_Boot_API.requests.item;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UploadItemRequest(
        UUID ownerId,
        UUID locationId,
        MultipartFile file
) {
}
