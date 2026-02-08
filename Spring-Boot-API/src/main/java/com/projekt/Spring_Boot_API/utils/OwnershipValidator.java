package com.projekt.Spring_Boot_API.utils;

import com.projekt.Spring_Boot_API.exceptions.folder.OwnerFolderMismatchException;
import com.projekt.Spring_Boot_API.exceptions.item.OwnerItemMismatchException;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.models.User;

public class OwnershipValidator {
    /**
     * Helper function that checks a user's ownership of a given item.
     *
     * @param user takes in a user object to compare with the owner of an item
     * @param item takes in an item object to compare with the user performing an action
     * @throws OwnerItemMismatchException if the user does not own the item
     */
    public static void checkItemOwnership(User user, Item item) {
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
    public static void checkFolderOwnership(User user, Folder folder) {
        if (!folder.getUser().getUserId()
                .equals(user.getUserId())) {
            throw new OwnerFolderMismatchException();
        }
    }
}
