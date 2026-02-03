package com.projekt.Spring_Boot_API.repositories;

import com.projekt.Spring_Boot_API.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User,UUID> {
    Optional<User> findByUserId(UUID userId);

    Optional<User> findByUsername(String username);
}

