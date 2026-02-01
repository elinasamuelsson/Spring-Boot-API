package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.folder.CreatedFolderDTO;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.requests.folder.CreateFolderRequest;
import com.projekt.Spring_Boot_API.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping ("/{userId}/create")
    public ResponseEntity<CreatedFolderDTO> createFolder(@PathVariable UUID userId,
                                                         @RequestParam(required = true) UUID parentFolderId,
                                                         @RequestBody CreateFolderRequest request) {
        Folder folder = folderService.createFolder(request.folderName(), parentFolderId, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreatedFolderDTO.from(folder));
    }

    @PutMapping("/{userId}/update/{folderId}")
    public ResponseEntity<?> updateFolder(@PathVariable UUID folderId,
                                          @RequestBody CreateFolderRequest request) {
        folderService.updateFolder(folderId, request.folderName());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/delete/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable UUID folderId) {
        folderService.deleteFolder(folderId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}") //TODO: Add DTO response entity to manage what shows up
    public ResponseEntity<List<Folder>> getSubFolders(@PathVariable UUID userId,
                                                      @RequestParam UUID parentFolderId) {
        return ResponseEntity
                .ok()
                .body(folderService.getSubFolders(userId, parentFolderId));
    }
}
