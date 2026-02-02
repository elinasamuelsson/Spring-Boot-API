package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.item.UploadedItemDTO;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.requests.item.UpdateItemRequest;
import com.projekt.Spring_Boot_API.requests.item.UploadItemRequest;
import com.projekt.Spring_Boot_API.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/upload")
    public ResponseEntity<UploadedItemDTO> uploadItem(@RequestBody UploadItemRequest request) {
        Item item = itemService.uploadItem(
                request.file().getName(),
                request.file(),
                (int) request.file().getSize(),
                request.locationId(),
                request.ownerId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UploadedItemDTO.from(item));
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable UUID itemId,
                                        @RequestBody UpdateItemRequest request) {
        itemService.updateItem(itemId, request.itemName(), request.itemLocationId());

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable UUID itemId) {
        itemService.deleteItem(itemId);

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/get-sub/{locationId}")
    public ResponseEntity<List<Item>> getItemsInFolder(@PathVariable UUID locationId) {
        return ResponseEntity
                .ok()
                .body(itemService.getItemsInFolder(locationId));
    }

    @GetMapping("/download/{itemId}")
    public ResponseEntity<?> downloadItem(@PathVariable UUID itemId) {
        Item item = itemService.downloadItem(itemId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", item.getItemName());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(item.getItemContents());
    }
}
