package com.projekt.Spring_Boot_API.repositories;

import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.Item;
import com.projekt.Spring_Boot_API.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findByFolderAndUser(Folder folder, User user);
}
