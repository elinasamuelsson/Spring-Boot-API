package com.projekt.Spring_Boot_API.requests.folder;

import java.util.UUID;

public record UpdateFolderRequest(
        String folderName,
        UUID parentFolderId
) {
}
