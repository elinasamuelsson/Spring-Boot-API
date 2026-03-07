package com.projekt.Spring_Boot_API.controllers;

import com.projekt.Spring_Boot_API.responses.folder.CreatedFolderResponse;
import com.projekt.Spring_Boot_API.responses.folder.FolderContentsResponse;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.requests.folder.CreateFolderRequest;
import com.projekt.Spring_Boot_API.requests.folder.UpdateFolderRequest;
import com.projekt.Spring_Boot_API.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users/me/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<EntityModel<CreatedFolderResponse>> createFolder(@RequestBody CreateFolderRequest request) {
        Folder folder = folderService.createFolder(request);

        CreatedFolderResponse response = CreatedFolderResponse.from(folder);

        EntityModel<CreatedFolderResponse> model = EntityModel.of(
                response,
                linkTo(methodOn(FolderController.class).getContents(folder.getFolderId())).withRel("folder")
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(model);
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
    public ResponseEntity<EntityModel<FolderContentsResponse>> getContents(@PathVariable UUID folderId) {
        FolderContentsResponse response = folderService.getContents(folderId);

        response.subFolders().forEach(
                subFolder -> subFolder.add(linkTo(methodOn(FolderController.class).getContents(subFolder.getFolderId())).withSelfRel())
        );

        response.items().forEach(
                item -> item.add(linkTo(methodOn(ItemController.class).downloadItem(item.getItemLocation(), item.getItemId())).withSelfRel())
        );

        EntityModel<FolderContentsResponse> model = EntityModel.of(
                response,
                linkTo(methodOn(FolderController.class).updateFolder(null, null)).withRel("update"),
                linkTo(methodOn(FolderController.class).deleteFolder(folderId)).withRel("delete"),
                linkTo(methodOn(FolderController.class).createFolder(null)).withRel("create"),
                linkTo(methodOn(ItemController.class).uploadItem(folderId, null)).withRel("upload item")
        );

        return ResponseEntity
                .ok()
                .body(model);
    }
}
