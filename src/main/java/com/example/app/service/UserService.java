package com.example.app.service;

import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.dto.UserServiceResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
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

        // Check if username already exists
        if (userRepository.findByName(user.getName()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new UserServiceResponse(
                            409,
                            "user already exists",
                            null
                    ));
        }

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Set default role
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
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
        

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

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
