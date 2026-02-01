package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.folder.FolderNameEmptyException;
import com.projekt.Spring_Boot_API.exceptions.folder.FolderNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.folder.UnauthorizedFolderActionException;
import com.projekt.Spring_Boot_API.exceptions.user.UserNotFoundException;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final IUserRepository userRepository;
    private final IFolderRepository folderRepository;

    public Folder createFolder(String folderName, UUID parentFolderId, UUID ownerId) {
        Folder parentFolder = null;

        if (parentFolderId != null) {
            parentFolder = folderRepository.findByFolderId(parentFolderId)
                    .orElseThrow(() -> new RuntimeException("Parent folder not found"));
        }

        User owner = userRepository.findByUserId(ownerId)
                .orElseThrow(UserNotFoundException::new);

        if (folderName == null || folderName.isBlank()) {
            throw new FolderNameEmptyException();
        }

        Folder folder = new Folder(folderName, parentFolder, owner);

        return folderRepository.save(folder);
    }

    public void updateFolder(UUID folderId, String folderName, UUID parentFolderId) {
        Folder folder = folderRepository.findByFolderId(folderId)
                .orElseThrow(FolderNotFoundException::new);

        if (folder.getParentFolder() == null) {
            throw new UnauthorizedFolderActionException("Root folder cannot be modified.");
        }

        if (parentFolderId != null) {
            Folder parentFolder = folderRepository.findByFolderId(parentFolderId)
                    .orElseThrow(FolderNotFoundException::new);
            folder.setParentFolder(parentFolder);
        }

        if (folderName != null && !folderName.isBlank()) {
            folder.setFolderName(folderName);
        }

        folderRepository.save(folder);
    }

    public void deleteFolder(UUID folderId) {
        Folder folder = folderRepository.findByFolderId(folderId)
                .orElseThrow(FolderNotFoundException::new);

        if (folder.getParentFolder() == null) {
            throw new UnauthorizedFolderActionException("Root folder cannot be deleted.");
        }

        folderRepository.delete(folder);
    }

    public List<Folder> getSubFolders(UUID userId, UUID parentFolderId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        Folder parentFolder = folderRepository.findByFolderId(parentFolderId)
                .orElseThrow(FolderNotFoundException::new);

        return folderRepository.findByParentFolderAndUser(parentFolder, user);
    }
}
