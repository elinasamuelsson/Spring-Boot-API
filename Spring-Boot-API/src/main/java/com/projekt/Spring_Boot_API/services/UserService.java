package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.user.*;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import com.projekt.Spring_Boot_API.requests.user.LoginUserRequest;
import com.projekt.Spring_Boot_API.requests.user.RegisterUserRequest;
import com.projekt.Spring_Boot_API.requests.user.UpdateUserRequest;
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
    public User registerUser(RegisterUserRequest request) {
        if (request.username() == null ||
                request.username().isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (request.password() == null ||
                request.password().isEmpty()) {
            throw new PasswordEmptyException();
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = userRepository.save(new User(
                request.username(),
                passwordHash)
        );

        folderRepository.save(new Folder("root", null, user));

        return user;
    }

    public String loginUser(LoginUserRequest request) {
        if (request.username() == null ||
                request.username().isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (request.password() == null ||
                request.password().isEmpty()) {
            throw new PasswordEmptyException();
        }

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user.getUserId());
    }

    public void updateUser(UpdateUserRequest request) {
        User authenticatedUser = authenticateUser();

        if (request.username() != null && !request.username().isBlank()) {
            authenticatedUser.setUsername(request.username());
        }

        if (request.password() != null &&
                !request.password().isBlank()) {
            String passwordHash = passwordEncoder.encode(request.password());
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

    public User getOwnUserData() {
        return userRepository.findByUserId(authenticateUser().getUserId())
                .orElseThrow(UserNotFoundException::new);
    }

    private User authenticateUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}