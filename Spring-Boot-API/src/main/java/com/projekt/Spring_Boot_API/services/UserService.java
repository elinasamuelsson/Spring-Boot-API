package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.user.*;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import com.projekt.Spring_Boot_API.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IFolderRepository folderRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (password == null || password.isEmpty()) {
            throw new PasswordEmptyException();
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = userRepository.save(new User(username, passwordHash));

        folderRepository.save(new Folder("root", null, user));

        return user;
    }

    public String loginUser(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (password == null || password.isEmpty()) {
            throw new PasswordEmptyException();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user.getUserId());
    }

    public void updateUser(String username, String password) {
        User authenticatedUser = authenticateUser();

        if (username != null && !username.isBlank()) {
            authenticatedUser.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            String passwordHash = passwordEncoder.encode(password);
            authenticatedUser.setPasswordHash(passwordHash);
        }
        userRepository.save(authenticatedUser);
    }

    public void deleteUser() {
        User authenticatedUser = authenticateUser();

        userRepository.delete(authenticatedUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID uuid) {
        return userRepository.findByUserId(uuid)
                .orElseThrow(UserNotFoundException::new);
    }

    private User authenticateUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}