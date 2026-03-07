package com.projekt.Spring_Boot_API.responses.folder;

import com.projekt.Spring_Boot_API.models.Folder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
public class SingleFolderResponse extends RepresentationModel<SingleFolderResponse> {
    private final UUID folderId;
    private final String folderName;
    private final UUID parentFolderId;

    public SingleFolderResponse(UUID folderId, String folderName, UUID parentFolderId) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.parentFolderId = parentFolderId;
    }

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
