package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.user.*;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import com.projekt.Spring_Boot_API.requests.user.LoginUserRequest;
import com.projekt.Spring_Boot_API.requests.user.RegisterUserRequest;
import com.projekt.Spring_Boot_API.requests.user.UpdateUserRequest;
import com.projekt.Spring_Boot_API.responses.user.AllUsersDataResponse;
import com.projekt.Spring_Boot_API.responses.user.LoggedInUserResponse;
import com.projekt.Spring_Boot_API.responses.user.RegisteredUserResponse;
import com.projekt.Spring_Boot_API.responses.user.SingleUserDataResponse;
import com.projekt.Spring_Boot_API.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * This service handles all logic related to users.
 *
 * It manages registering, logging in, updating, deleting and sending user data on request.
 *
 * @author Elina Samuelsson
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IFolderRepository folderRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers new users to the application, validates the given information,
     * hashes the chosen password, calls on userRepository to save, and lastly
     * creates a root folder for the user.
     *
     * @param request takes in the full request from UserController to avoid sending multiple parameters for
     *                better readability
     * @return a fully built response DTO containing username and a newly generated UUID
     * @throws UsernameEmptyException if the username field is empty
     * @throws UsernameNotApprovedException if the username doesn't match given parameters,
     *                                      or already exists in the database
     * @throws PasswordEmptyException if the password field is empty
     * @throws PasswordNotApprovedException if the password doesn't match given parameters
     */
    @Transactional
    public RegisteredUserResponse registerUser(RegisterUserRequest request) {
        if (request.username() == null ||
                request.username().isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (userRepository.findByUsername(request.username()).isPresent() ||
                request.username().length() < 5) {
            throw new UsernameNotApprovedException();
        }

        if (request.password() == null ||
                request.password().isEmpty()) {
            throw new PasswordEmptyException();
        }

        //validates that the given password contains a number, a special character, and is longer than 8 characters
        if (!request.password().matches(".*[0-9].*") ||
                !request.password().matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*") ||
                request.password().length() < 8) {
            throw new PasswordNotApprovedException();
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = userRepository.save(new User(
                request.username(),
                passwordHash)
        );

        //creates a root folder for the newly registered user
        folderRepository.save(new Folder("root", null, user));

        return RegisteredUserResponse.from(user);
    }

    /**
     * Logs in a user to the API.
     *
     * @param request takes in the full request from UserController to avoid sending multiple parameters for
     *                better readability
     * @return a fully built response DTO containing the username and their JWT token
     * @throws UsernameEmptyException if the username field is empty
     * @throws PasswordEmptyException if the password field is empty
     * @throws UserNotFoundException if user is not found in the database
     * @throws InvalidCredentialsException if the given password doesn't match the stored password
     */
    public LoggedInUserResponse loginUser(LoginUserRequest request) {
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

        String token = jwtService.generateToken(user.getUserId());

        return LoggedInUserResponse.from(
                new HashMap<>(Map.of(
                        "username", user.getUsername(),
                        "token", token
                ))
        );
    }

    /**
     * Updates logged in user's data.
     *
     * @param request takes in the full request from UserController to avoid sending multiple parameters for
     *                better readability
     * @throws UsernameEmptyException if the username field is empty
     * @throws UsernameNotApprovedException if the username doesn't match given parameters,
     *                                      or already exists in the database
     * @throws PasswordEmptyException if the password field is empty
     * @throws PasswordNotApprovedException if the password doesn't match given parameters
     */
    public void updateUser(UpdateUserRequest request) {
        User authenticatedUser = authenticateUser();

        if (request.username() != null && !request.username().isBlank()) {
            if (userRepository.findByUsername(request.username()).isPresent() ||
                    request.username().length() < 5) {
                throw new UsernameNotApprovedException();
            }
            authenticatedUser.setUsername(request.username());
        }

        if (request.password() != null && !request.password().isEmpty()) {
            // validates that password contains a number and a special character, and is at least 8 characters
            if (!request.password().matches(".*[0-9].*") ||
                    !request.password().matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*") ||
                    request.password().length() < 8) {
                throw new PasswordNotApprovedException();
            }

            String passwordHash = passwordEncoder.encode(request.password());
            authenticatedUser.setPasswordHash(passwordHash);
        }
        userRepository.save(authenticatedUser);
    }

    /**
     * Deletes the logged-in user's data from the database.
     */
    public void deleteUser() {
        User authenticatedUser = authenticateUser();

        userRepository.delete(authenticatedUser);
    }

    /**
     * Gets all existing user's data from the database.
     * @return a fully built response DTO containing a list of users, showing their
     *         userId, username, and root folder
     */
    public AllUsersDataResponse getAllUsers() {
        return AllUsersDataResponse.from(userRepository.findAll());
    }

    /**
     * Gets the logged-in user's data from the database.
     * @return a fully built response DTO containing their userId, username, and root folder
     * @throws UserNotFoundException if the user could not be found in the database
     */
    public SingleUserDataResponse getOwnUserData() {
        return SingleUserDataResponse
                .from(userRepository.findByUserId(authenticateUser().getUserId())
                        .orElseThrow(UserNotFoundException::new));
    }

    /**
     * Helper method that fetches the currently authenticated user.
     *
     * @return User object of the fetch result
     */
    private User authenticateUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}