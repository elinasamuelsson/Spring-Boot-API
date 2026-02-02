package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.item.UploadedItemDTO;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/{userId}/upload")
    public ResponseEntity<UploadedItemDTO> uploadItem(@PathVariable UUID userId,
                                                      @RequestParam("file") MultipartFile file,
                                                      @RequestParam UUID locationId) {
        Item item = itemService.uploadItem(
                file.getName(),
                file,
                (int) file.getSize(),
                locationId,
                userId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UploadedItemDTO.from(item));
    }

    @PutMapping("{userId}/update/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable UUID itemId,
                                        @RequestParam(required = false) String itemName,
                                        @RequestParam(required = false) UUID itemLocationId) {
        itemService.updateItem(itemId, itemName, itemLocationId);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("{userId}/delete/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable UUID itemId) {
        itemService.deleteItem(itemId);

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("{userId}") //TODO: Add DTO response entity to manage what shows up
    public ResponseEntity<List<Item>> getItemsInFolder(@PathVariable UUID userId,
                                                       @RequestParam UUID locationId) {
        return ResponseEntity
                .ok()
                .body(itemService.getItemsInFolder(userId, locationId));
    }

    @GetMapping("{userId}/download/{itemId}")
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
