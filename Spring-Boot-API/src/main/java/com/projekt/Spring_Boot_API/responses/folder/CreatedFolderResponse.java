package com.projekt.Spring_Boot_API.responses.folder;

import com.projekt.Spring_Boot_API.models.Folder;

public record CreatedFolderResponse(
        String folderName
) {
    public static CreatedFolderResponse from(Folder folder) {
        return new CreatedFolderResponse(folder.getFolderName());
    }
}
