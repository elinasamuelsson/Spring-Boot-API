package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.item.UploadedItemDTO;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("{userId}") //TODO: Add DTO response entity to manage what shows up
    public ResponseEntity<List<Item>> getItems(@PathVariable UUID userId,
                                               @RequestParam UUID locationId) {
        return ResponseEntity
                .ok()
                .body(itemService.getItemsInFolder(userId, locationId));
    }
}
