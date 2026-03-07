package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.folder.CreatedFolderResponse;
import com.projekt.Spring_Boot_API.responses.folder.FolderContentsResponse;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.requests.folder.CreateFolderRequest;
import com.projekt.Spring_Boot_API.requests.folder.UpdateFolderRequest;
import com.projekt.Spring_Boot_API.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<CreatedFolderResponse> createFolder(@RequestBody CreateFolderRequest request) {
        Folder folder = folderService.createFolder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreatedFolderResponse.from(folder));
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<Void> updateFolder(@PathVariable UUID folderId,
                                          @RequestBody UpdateFolderRequest request) {
        folderService.updateFolder(folderId, request);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable UUID folderId) {
        folderService.deleteFolder(folderId);

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderContentsResponse> getContents(@PathVariable UUID folderId) {

        FolderContentsResponse dto = folderService.getContents(folderId);

        return ResponseEntity
                .ok()
                .body(dto);
    }
}
