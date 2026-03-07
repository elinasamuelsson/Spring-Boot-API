package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.item.UploadedItemResponse;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.requests.item.UpdateItemRequest;
import com.projekt.Spring_Boot_API.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/folders")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/{folderId}/items")
    public ResponseEntity<UploadedItemResponse> uploadItem(@PathVariable UUID folderId,
                                                           @RequestParam("file") MultipartFile file) {
        UploadedItemResponse response = itemService.uploadItem(folderId, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{folderId}/items/{itemId}")
    public ResponseEntity<Void> updateItem(@PathVariable UUID folderId,
                                           @PathVariable UUID itemId,
                                           @RequestBody UpdateItemRequest request) {
        itemService.updateItem(itemId, folderId, request);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{folderId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID folderId,
                                           @PathVariable UUID itemId) {
        itemService.deleteItem(itemId, folderId);

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/{folderId}/items/{itemId}")
    public ResponseEntity<byte[]> downloadItem(@PathVariable UUID folderId,
                                               @PathVariable UUID itemId) {
        Item item = itemService.downloadItem(itemId, folderId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", item.getItemName());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(item.getItemContents());
    }
}
