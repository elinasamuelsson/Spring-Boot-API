package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.user.PasswordEmptyException;
import com.projekt.Spring_Boot_API.exceptions.user.UserNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.user.UsernameEmptyException;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import com.projekt.Spring_Boot_API.requests.user.InvalidCredentialsException;
import com.projekt.Spring_Boot_API.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final FolderService folderService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (password == null || password.isEmpty()) {
            throw new PasswordEmptyException();
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = userRepository.save(new User(username, passwordHash));

        folderService.createFolder("root", null, user.getUserId());

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

    public void updateUser(UUID userId, String username, String password) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            String passwordHash = passwordEncoder.encode(password);
            user.setPasswordHash(passwordHash);
        }
        userRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID uuid) {
        return userRepository.findByUserId(uuid)
                .orElseThrow(UserNotFoundException::new);
    }
}