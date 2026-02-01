package com.projekt.Spring_Boot_API.repositories;

import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IFolderRepository extends JpaRepository<Folder, UUID> {
    Optional<Folder> findByFolderId(UUID folderId);

    List<Folder> findByParentFolderAndUser(Folder parentFolder, User user);
}
