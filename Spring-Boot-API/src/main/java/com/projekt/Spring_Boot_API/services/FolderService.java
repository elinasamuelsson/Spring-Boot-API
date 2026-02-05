package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.dtos.folder.FolderContentsDTO;
import com.projekt.Spring_Boot_API.exceptions.folder.FolderNameEmptyException;
import com.projekt.Spring_Boot_API.exceptions.folder.FolderNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.folder.OwnerFolderMismatchException;
import com.projekt.Spring_Boot_API.exceptions.folder.UnauthorizedFolderActionException;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IItemRepository;
import com.projekt.Spring_Boot_API.requests.folder.CreateFolderRequest;
import com.projekt.Spring_Boot_API.requests.folder.UpdateFolderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final IFolderRepository folderRepository;
    private final IItemRepository itemRepository;

    public Folder createFolder(CreateFolderRequest request) {
        Folder parentFolder = folderRepository.findByFolderId(request.parentFolderId())
                .orElseThrow(FolderNotFoundException::new);

        User user = authenticateUser();
        checkFolderOwnership(user, parentFolder);

        if (request.folderName() == null || request.folderName().isBlank()) {
            throw new FolderNameEmptyException();
        }

        Folder folder = new Folder(request.folderName(), parentFolder, user);

        return folderRepository.save(folder);
    }

    public void updateFolder(UUID folderId, UpdateFolderRequest request) {
        Folder folder = folderRepository.findByFolderId(folderId)
                .orElseThrow(FolderNotFoundException::new);

        if (folder.getParentFolder() == null) {
            throw new UnauthorizedFolderActionException();
        }

        User user = authenticateUser();

        checkFolderOwnership(user, folder);

        if (request.parentFolderId() != null) {
            Folder parentFolder = folderRepository.findByFolderId(request.parentFolderId())
                    .orElseThrow(FolderNotFoundException::new);
            checkFolderOwnership(user, parentFolder);
            folder.setParentFolder(parentFolder);
        }

        if (request.folderName() != null && !request.folderName().isBlank()) {
            folder.setFolderName(request.folderName());
        }

        folderRepository.save(folder);
    }

    public void deleteFolder(UUID folderId) {
        Folder folder = folderRepository.findByFolderId(folderId)
                .orElseThrow(FolderNotFoundException::new);

        if (folder.getParentFolder() == null) {
            throw new UnauthorizedFolderActionException();
        }

        checkFolderOwnership(authenticateUser(), folder);

        folderRepository.delete(folder);
    }

    public FolderContentsDTO getContents(UUID parentFolderId) {
        Folder parentFolder = folderRepository.findByFolderId(parentFolderId)
                .orElseThrow(FolderNotFoundException::new);

        checkFolderOwnership(authenticateUser(), parentFolder);

        return new FolderContentsDTO(
                folderRepository.findByParentFolder(parentFolder),
                itemRepository.findByFolder(parentFolder)
        );
    }

    private User authenticateUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void checkFolderOwnership(User user, Folder folder) {
        if (!user.getUserId()
                .equals(folder.getUser().getUserId())) {
            throw new OwnerFolderMismatchException();
        }
    }
}
