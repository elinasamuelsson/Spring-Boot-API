package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.responses.folder.FolderContentsResponse;
import com.projekt.Spring_Boot_API.exceptions.folder.FolderNameEmptyException;
import com.projekt.Spring_Boot_API.exceptions.folder.FolderNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.folder.UnauthorizedFolderActionException;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.requests.folder.CreateFolderRequest;
import com.projekt.Spring_Boot_API.requests.folder.UpdateFolderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.projekt.Spring_Boot_API.utils.OwnershipValidator.checkFolderOwnership;
import static com.projekt.Spring_Boot_API.utils.UserAuthenticator.authenticateUser;

/**
 * This service handles all logic related to folders.
 *
 * It manages creating, updating, deleting and sending folder data on request.
 *
 * @author Elina Samuelsson
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class FolderService {
    private final IFolderRepository folderRepository;

    /**
     * Creates new folders in a given parent folder, validates that the folder
     * has a name, and assigns the authenticated user as the owner.
     *
     * @param request takes in the full request from FolderController to avoid sending multiple paramters for
     *                better readability
     * @throws FolderNotFoundException if the parent folder could not be found in the database
     * @throws FolderNameEmptyException if the folderName field is empty
     */
    public Folder createFolder(CreateFolderRequest request) {
        Folder parentFolder = folderRepository
                .findByFolderId(request.parentFolderId())
                .orElseThrow(FolderNotFoundException::new);

        User user = authenticateUser();
        checkFolderOwnership(user, parentFolder);

        if (request.folderName() == null || request.folderName().isBlank()) {
            throw new FolderNameEmptyException();
        }

        Folder folder = new Folder(request.folderName(), parentFolder, user);

        return folderRepository.save(folder);
    }

    /**
     * Updates the given folder's data.
     *
     * @param folderId takes in the id of the parent folder
     * @param request takes in the full request from FolderController to avoid sending multiple parameters for
     *                better readability
     * @throws FolderNotFoundException if the parent folder could not be found in the database
     * @throws UnauthorizedFolderActionException if a user tries to update a folder they are not the owner of
     */
    public void updateFolder(UUID folderId, UpdateFolderRequest request) {
        Folder folder = folderRepository
                .findByFolderId(folderId)
                .orElseThrow(FolderNotFoundException::new);

        //if folder has no parent folder it has to be the root folder
        if (folder.getParentFolder() == null) {
            throw new UnauthorizedFolderActionException();
        }

        User user = authenticateUser();
        checkFolderOwnership(user, folder);

        if (request.parentFolderId() != null) {
            Folder parentFolder = folderRepository
                    .findByFolderId(request.parentFolderId())
                    .orElseThrow(FolderNotFoundException::new);

            checkFolderOwnership(user, parentFolder);

            folder.setParentFolder(parentFolder);
        }

        if (request.folderName() != null && !request.folderName().isBlank()) {
            folder.setFolderName(request.folderName());
        }

        folderRepository.save(folder);
    }

    /**
     * Deletes a folder and all of its child folders and items from the database
     *
     * @param folderId takes in the folder's id
     * @throws FolderNotFoundException if the folder could not be found in the database
     * @throws UnauthorizedFolderActionException if the folder is the root folder
     */
    public void deleteFolder(UUID folderId) {
        Folder folder = folderRepository
                .findByFolderId(folderId)
                .orElseThrow(FolderNotFoundException::new);

        //if folder has no parent folder it has to be the root folder
        if (folder.getParentFolder() == null) {
            throw new UnauthorizedFolderActionException();
        }

        checkFolderOwnership(authenticateUser(), folder);

        folderRepository.delete(folder);
    }

    /**
     * Get the contents of a given folder.
     *
     * @param parentFolderId takes in the id of the folder whose contents will be viewed
     * @return a fully built response DTO containing a list of child folders and a list
     *         of child items
     * @throws FolderNotFoundException if the folder could not be found in the database
     */
    public FolderContentsResponse getContents(UUID parentFolderId) {
        Folder parentFolder = folderRepository
                .findByFolderId(parentFolderId)
                .orElseThrow(FolderNotFoundException::new);

        checkFolderOwnership(authenticateUser(), parentFolder);

        return FolderContentsResponse
                .from(parentFolder.getSubFolders(), parentFolder.getItems());
    }
}
