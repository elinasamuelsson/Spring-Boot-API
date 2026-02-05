package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.item.UploadedItemResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/upload")
    public ResponseEntity<UploadedItemResponse> uploadItem(@ModelAttribute UploadItemRequest request,
                                                           @RequestParam("file") MultipartFile file) {
        UploadedItemResponse response = itemService.uploadItem(request.locationId(), file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable UUID itemId,
                                        @RequestBody UpdateItemRequest request) {
        itemService.updateItem(itemId, request);

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

    @GetMapping("/download/{itemId}")
    public ResponseEntity<byte[]> downloadItem(@PathVariable UUID itemId) {
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
