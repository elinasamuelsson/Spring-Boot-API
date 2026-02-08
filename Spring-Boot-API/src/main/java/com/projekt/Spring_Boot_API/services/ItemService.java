package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.folder.FolderNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.folder.OwnerFolderMismatchException;
import com.projekt.Spring_Boot_API.exceptions.item.FileUploadFailException;
import com.projekt.Spring_Boot_API.exceptions.item.ItemNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.item.OwnerItemMismatchException;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IItemRepository;
import com.projekt.Spring_Boot_API.requests.item.UpdateItemRequest;
import com.projekt.Spring_Boot_API.responses.item.UploadedItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * This service handles all logic related to items.
 *
 * It manages uploading, updating, deleting and downloading items on request.
 *
 * @author Elina Samuelsson
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ItemService {
    private final IFolderRepository folderRepository;
    private final IItemRepository itemRepository;

    /**
     * Uploads item into a given parent folder.
     *
     * @param locationId takes in the id of the item's parent folder
     * @param file takes in the file to be uploaded
     * @throws FileUploadFailException if the file has failed to upload
     * @throws FolderNotFoundException if the parent folder could not be found in the database
     */
    public UploadedItemResponse uploadItem(UUID locationId, MultipartFile file) {
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new FileUploadFailException();
        }

        User owner = authenticateUser();

        Folder location = folderRepository.findByFolderId(locationId)
                .orElseThrow(FolderNameEmptyException::new);

        checkFolderOwnership(owner, location);

        Item item = new Item(
                file.getOriginalFilename(),
                fileBytes,
                (int) file.getSize(),
                location,
                owner);

        return UploadedItemResponse.from(
                itemRepository.save(item)
        );
    }

    /**
     * Updates the given item's data.
     *
     * @param itemId takes in the id of the item to be modified
     * @param request takes in the full request from ItemController to avoid sending multiple parameters for
     *                better readability
     * @throws ItemNotFoundException if the item could not be found in the database
     * @throws FolderNotFoundException if the parent folder could not be found in the database
     */
    public void updateItem(UUID itemId, UpdateItemRequest request) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(ItemNotFoundException::new);

        User user = authenticateUser();

        checkItemOwnership(user, item);

        if (request.itemLocationId() != null) {
            Folder folder = folderRepository.findByFolderId(request.itemLocationId())
                    .orElseThrow(FolderNameEmptyException::new);

            checkFolderOwnership(user, folder);

            item.setFolder(folder);
        }

        if (request.itemName() != null && !request.itemName().isBlank()) {
            item.setItemName(request.itemName());
        }

        itemRepository.save(item);
    }

    /**
     * Deletes the item from the database.
     *
     * @param itemId takes in the item to be deleted
     * @throws ItemNotFoundException if the item could not be found in the database
     */
    public void deleteItem(UUID itemId) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(ItemNotFoundException::new);

        User user = authenticateUser();

        checkItemOwnership(user, item);

        itemRepository.delete(item);
    }

    /**
     * Gets a single item.
     *
     * @param itemId takes in the id of the item to be viewed/downloaded
     * @return an Item object
     * @throws ItemNotFoundException if the item could not be found in the database
     */
    public Item downloadItem(UUID itemId) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(ItemNotFoundException::new);

        User user = authenticateUser();

        checkItemOwnership(user, item);

        return item;
    }

    /**
     * Helper method that fetches the currently authenticated user.
     *
     * @return User object of the fetch result
     */
    private User authenticateUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    /**
     * Helper function that checks a user's ownership of a given item.
     *
     * @param user takes in a user object to compare with the owner of an item
     * @param item takes in an item object to compare with the user performing an action
     * @throws OwnerItemMismatchException if the user does not own the item
     */
    private void checkItemOwnership(User user, Item item) {
        if (!item.getUser().getUserId()
                .equals(user.getUserId())) {
            throw new OwnerItemMismatchException();
        }
    }

    /**
     * Helper function that checks a user's ownership of a given folder.
     *
     * @param user takes in a user object to compare with the owner of a folder
     * @param folder takes in a folder object to compare with the user performing an action
     * @throws OwnerFolderMismatchException if the user does not own the folder
     */
    private void checkFolderOwnership(User user, Folder folder) {
        if (!folder.getUser().getUserId()
                .equals(user.getUserId())) {
            throw new OwnerFolderMismatchException();
        }
    }
}
