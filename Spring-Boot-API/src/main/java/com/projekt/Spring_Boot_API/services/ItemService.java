package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.folder.FolderNameEmptyException;
import com.projekt.Spring_Boot_API.exceptions.folder.OwnerFolderMismatchException;
import com.projekt.Spring_Boot_API.exceptions.item.ItemNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.item.OwnerItemMismatchException;
import com.projekt.Spring_Boot_API.exceptions.user.UserNotFoundException;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IItemRepository;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final IUserRepository userRepository;
    private final IFolderRepository folderRepository;
    private final IItemRepository itemRepository;

    public Item uploadItem(String name, MultipartFile file, int size, UUID folderId) {
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            System.out.println("Could not read file.");
            e.printStackTrace();
        }

        if (fileBytes == null) {
            throw new RuntimeException("File has no contents.");
        }

        User owner = authenticateUser();

        Folder location = folderRepository.findByFolderId(folderId)
                .orElseThrow(FolderNameEmptyException::new);

        checkFolderOwnership(owner, location);

        Item item = new Item(name, fileBytes, size, location, owner);

        return itemRepository.save(item);
    }

    public void updateItem(UUID itemId, String itemName, UUID itemLocationId) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(RuntimeException::new);

        User user = authenticateUser();

        checkItemOwnership(user, item);

        if (itemLocationId != null) {
            Folder folder = folderRepository.findByFolderId(itemLocationId)
                    .orElseThrow(FolderNameEmptyException::new);

            checkFolderOwnership(user, folder);

            item.setFolder(folder);
        }

        if (itemName != null && !itemName.isBlank()) {
            item.setItemName(itemName);
        }

        itemRepository.save(item);
    }

    public void deleteItem(UUID itemId) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(RuntimeException::new);

        User user = authenticateUser();

        checkItemOwnership(user, item);

        itemRepository.delete(item);
    }

    public Item downloadItem(UUID itemId) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(ItemNotFoundException::new);

        User user = authenticateUser();

        checkItemOwnership(user, item);

        return item;
    }

    private User authenticateUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void checkItemOwnership(User user, Item item) {
        if (!item.getUser().getUserId()
                .equals(user.getUserId())) {
            throw new OwnerItemMismatchException();
        }
    }

    private void checkFolderOwnership(User user, Folder folder) {
        if (!folder.getUser().getUserId()
                .equals(user.getUserId())) {
            throw new OwnerFolderMismatchException();
        }
    }
}
