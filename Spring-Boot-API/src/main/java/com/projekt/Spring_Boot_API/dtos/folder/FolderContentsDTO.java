package com.projekt.Spring_Boot_API.dtos.folder;

import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.Item;

import java.util.List;

public record FolderContentsDTO(
        List<Folder> subFolders,
        List<Item> items
) {
}
