package com.projekt.Spring_Boot_API.services;

import com.projekt.Spring_Boot_API.exceptions.PasswordEmptyException;
import com.projekt.Spring_Boot_API.exceptions.UserNotFoundException;
import com.projekt.Spring_Boot_API.exceptions.UsernameEmptyException;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private IUserRepository userRepository;

    public User registerUser(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new UsernameEmptyException();
        }

        if (password == null || password.isEmpty()) {
            throw new PasswordEmptyException();
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        return userRepository.save(
                new User(username, passwordHash)
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID uuid) {
        return userRepository.findByUserId(uuid)
                .orElseThrow(UserNotFoundException::new);
    }
}