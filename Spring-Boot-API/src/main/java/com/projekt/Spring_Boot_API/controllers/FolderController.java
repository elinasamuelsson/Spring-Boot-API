package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.folder.CreatedFolderDTO;
import com.projekt.Spring_Boot_API.responses.folder.FolderContentsDTO;
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
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<CreatedFolderDTO> createFolder(@RequestBody CreateFolderRequest request) {
        Folder folder = folderService.createFolder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreatedFolderDTO.from(folder));
    }

    @PutMapping("/update/{folderId}")
    public ResponseEntity<?> updateFolder(@PathVariable UUID folderId,
                                          @RequestBody UpdateFolderRequest request) {
        folderService.updateFolder(
                folderId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable UUID folderId) {
        folderService.deleteFolder(folderId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-contents/{folderId}")
    public ResponseEntity<FolderContentsDTO> getContents(@PathVariable UUID folderId) {

        FolderContentsDTO dto = folderService.getContents(folderId);

        return ResponseEntity
                .ok()
                .body(dto);
    }
}
