package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.user.InvalidCredentialsException;
import com.projekt.Spring_Boot_API.exceptions.user.PasswordEmptyException;
import com.projekt.Spring_Boot_API.exceptions.user.UserNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.user.UsernameEmptyException;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import com.projekt.Spring_Boot_API.requests.user.LoginUserRequest;
import com.projekt.Spring_Boot_API.responses.user.LoggedInUserResponse;
import com.projekt.Spring_Boot_API.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

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
        if (request.username() == null || request.username().isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (request.password() == null || request.password().isEmpty()) {
            throw new PasswordEmptyException();
        }

        User user = userRepository
                .findByUsername(request.username())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getUserId());

        return LoggedInUserResponse
                .from(new HashMap<>(Map.of(
                        "username", user.getUsername(),
                        "token", token))
                );
    }
}
