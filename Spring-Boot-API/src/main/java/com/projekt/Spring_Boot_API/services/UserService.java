package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.user.*;
import com.projekt.Spring_Boot_API.models.Folder;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IFolderRepository;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import com.projekt.Spring_Boot_API.requests.user.RegisterUserRequest;
import com.projekt.Spring_Boot_API.requests.user.UpdateUserRequest;
import com.projekt.Spring_Boot_API.responses.user.AllUsersDataResponse;
import com.projekt.Spring_Boot_API.responses.user.RegisteredUserResponse;
import com.projekt.Spring_Boot_API.responses.user.SingleUserDataResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.projekt.Spring_Boot_API.utils.UserAuthenticator.authenticateUser;

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
        if (request.username() == null || request.username().isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (userRepository.findByUsername(request.username()).isPresent() || request.username().length() < 5) {
            throw new UsernameNotApprovedException();
        }

        if (request.password() == null || request.password().isEmpty()) {
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
        folderRepository.save(new Folder(
                "root",
                null,
                user)
        );

        return RegisteredUserResponse
                .from(user);
    }

    /**
     * Registers new users coming in via Oauth, validates the username doesn't already exist,
     * calls on userRepository to save, and lastly creates a root folder for the user.
     *
     * @param email takes in email to be used as the username
     * @param oidcId takes in the oidcId for the user provided by the Oauth provider
     * @param oidcProvider takes in who Oauth provider is
     * @return the newly created and saved user
     * @throws UsernameNotApprovedException if the username doesn't match given parameters,
     *                                      or already exists in the database
     */
    public User registerOidcUser(String email, String oidcId, String oidcProvider) {
        if (userRepository.findByUsername(email).isPresent()) {
            throw new UsernameNotApprovedException();
        }

        User user = userRepository.save(new User(email, oidcId, oidcProvider));

        folderRepository.save(new Folder(
                "root",
                null,
                user)
        );

        return user;
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
        User authenticatedUser = (User) authenticateUser();

        if (request.username() != null && !request.username().isBlank()) {
            if (userRepository.findByUsername(request.username()).isPresent() || request.username().length() < 5) {
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
        User authenticatedUser = (User) authenticateUser();

        userRepository.delete(authenticatedUser);
    }

    /**
     * Gets all existing user's data from the database.
     * @return a fully built response DTO containing a list of users, showing their
     *         userId, username, and root folder
     */
    public AllUsersDataResponse getAllUsers() {
        return AllUsersDataResponse
                .from(userRepository.findAll());
    }

    /**
     * Gets the logged-in user's data from the database.
     * @return a fully built response DTO containing their userId, username, and root folder
     * @throws UserNotFoundException if the user could not be found in the database
     */
    public SingleUserDataResponse getOwnUserData() {
        User user = (User) authenticateUser();

        return SingleUserDataResponse
                .from(userRepository
                        .findByUserId(user.getUserId())
                        .orElseThrow(UserNotFoundException::new));
    }

    public Optional<User> getUserByOidcId(String oidcId, String oidcProvider) {
        return userRepository.findByOidcIdAndOidcProvider(oidcId, oidcProvider);
    }
}