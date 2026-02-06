package com.projekt.Spring_Boot_API.responses.folder;

import com.projekt.Spring_Boot_API.models.Folder;

import java.util.UUID;

public record SingleFolderResponse(
        UUID folderId,
        String folderName,
        UUID parentFolderId
) {
    public static SingleFolderResponse from(Folder folder) {
        UUID parentFolderId = null;

        if (folder.getParentFolder() != null) {
            parentFolderId = folder.getParentFolder().getFolderId();
        }

        return new SingleFolderResponse(
                folder.getFolderId(),
                folder.getFolderName(),
                parentFolderId
        );
    }
}
