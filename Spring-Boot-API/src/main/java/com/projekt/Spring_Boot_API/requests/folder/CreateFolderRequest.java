package com.projekt.Spring_Boot_API.requests.folder;

import java.util.UUID;

public record CreateFolderRequest(
        String folderName,
        UUID parentFolderId
) {
}
