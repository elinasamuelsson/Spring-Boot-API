package com.projekt.Spring_Boot_API.responses.folder;

import com.projekt.Spring_Boot_API.models.Folder;

public record CreatedFolderDTO(
        String folderName
) {
    public static CreatedFolderDTO from(Folder folder) {
        return new CreatedFolderDTO(folder.getFolderName());
    }
}
