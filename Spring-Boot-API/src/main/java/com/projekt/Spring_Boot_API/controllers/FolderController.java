package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.dtos.folder.CreatedFolderDTO;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.requests.folder.CreateFolderRequest;
import com.projekt.Spring_Boot_API.requests.folder.UpdateFolderRequest;
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

    @PostMapping("/create") //TODO: validate that user can only create folders in their own folders
    public ResponseEntity<CreatedFolderDTO> createFolder(@RequestBody CreateFolderRequest request) {
        Folder folder = folderService.createFolder(
                request.folderName(),
                request.parentFolderId(),
                request.ownerId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreatedFolderDTO.from(folder));
    }

    @PutMapping("/update/{folderId}")
    public ResponseEntity<?> updateFolder(@PathVariable UUID folderId,
                                          @RequestBody UpdateFolderRequest request) {
        folderService.updateFolder(
                folderId,
                request.folderName(),
                request.parentFolderId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable UUID folderId) {
        folderService.deleteFolder(folderId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-sub/{folderId}") //TODO: validate that user can only see their own folders and files
    public ResponseEntity<List<Folder>> getSubFolders(@PathVariable UUID folderId) {
        return ResponseEntity
                .ok()
                .body(folderService.getSubFolders(folderId));
    }
}
