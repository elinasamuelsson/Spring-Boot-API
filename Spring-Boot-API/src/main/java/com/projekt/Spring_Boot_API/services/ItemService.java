package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.folder.FolderNameEmptyException;
import com.projekt.Spring_Boot_API.exceptions.user.UserNotFoundException;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IItemRepository;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final IUserRepository userRepository;
    private final IFolderRepository folderRepository;
    private final IItemRepository itemRepository;

    public Item uploadItem(String name, MultipartFile file, int size, UUID folderId, UUID userId) {
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

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Folder folder = folderRepository.findByFolderId(folderId)
                .orElseThrow(FolderNameEmptyException::new);

        Item item = new Item(name, fileBytes, size, folder, user);

        return itemRepository.save(item);
    }

    public List<Item> getItemsInFolder(UUID userId, UUID folderId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        Folder folder = folderRepository.findByFolderId(folderId)
                .orElseThrow(FolderNameEmptyException::new);

        return itemRepository.findByFolderAndUser(folder, user);
    }
}
