package com.projekt.Spring_Boot_API.responses.folder;

import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.responses.item.SingleItemResponse;

import java.util.List;

public record FolderContentsResponse(
        List<SingleFolderResponse> subFolders,
        List<SingleItemResponse> items
) {
    public static FolderContentsResponse from(List<Folder> folders, List<Item> items) {
        return new FolderContentsResponse(
                folders.stream().map(SingleFolderResponse::from).toList(),
                items.stream().map(SingleItemResponse::from).toList()
        );
    }
}
