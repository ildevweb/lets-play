package com.example.app.service;

import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.dto.UserServiceResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserServiceResponse getUser(String id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return new UserServiceResponse(
                404, 
                "user not found",
                null);
        }

        return new UserServiceResponse(
            200, 
            "User Found",
            user.get());
    }


    public ResponseEntity<UserServiceResponse> createUser(User user) {
        user.setName(user.getName().trim());
        user.setEmail(user.getEmail().trim().toLowerCase(Locale.ROOT));

        if (userRepository.findByName(user.getName()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new UserServiceResponse(
                            409,
                            "user already exists",
                            null
                    ));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new UserServiceResponse(409, "email already exists", null));
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        } else {
            user.setRole(user.getRole().toUpperCase(Locale.ROOT));
        }

        // Save user
        User savedUser = userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserServiceResponse(
                        201,
                        "User created successfully",
                        savedUser
                ));
    }


    public UserServiceResponse putUser(String id, User request) {

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            return new UserServiceResponse(
                404,
                "User not found",
                null);
        }

        User user = existingUser.get();
        request.setName(request.getName().trim());
        request.setEmail(request.getEmail().trim().toLowerCase(Locale.ROOT));

        if (!user.getName().equals(request.getName())
            && userRepository.findByName(request.getName()).isPresent()) {
            return new UserServiceResponse(409, "username already exists", null);
        }
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
            && userRepository.existsByEmail(request.getEmail())) {
            return new UserServiceResponse(409, "email already exists", null);
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRole() != null) {
            user.setRole(request.getRole().toUpperCase(Locale.ROOT));
        }

        return new UserServiceResponse(
            200,
            "User updated successfully",
            userRepository.save(user));
    }


    public UserServiceResponse deleteUser(String id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return new UserServiceResponse(
                404,
                "User not found",
                null
            );
        }

        userRepository.delete(user.get());
        
        return new UserServiceResponse(
            200,
            "User successfully deleted");
    }
}
