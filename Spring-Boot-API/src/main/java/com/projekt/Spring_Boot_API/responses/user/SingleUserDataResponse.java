package com.projekt.Spring_Boot_API.responses.user;

import com.projekt.Spring_Boot_API.exceptions.folder.FolderNotFoundException;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.responses.folder.SingleFolderResponse;

import java.util.UUID;

public record SingleUserDataResponse(
        UUID userId,
        String username,
        SingleFolderResponse rootFolder
) {
    public static SingleUserDataResponse from(User user) {
        SingleFolderResponse rootFolder = user.getFolders()
                .stream()
                .filter(folder -> folder.getParentFolder() == null)
                .map(SingleFolderResponse::from)
                .findFirst()
                .orElseThrow(FolderNotFoundException::new);

        return new SingleUserDataResponse(
                user.getUserId(),
                user.getUsername(),
                rootFolder
        );
    }
}
