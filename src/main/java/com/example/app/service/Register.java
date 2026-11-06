package com.example.app.service;

import com.example.app.dto.RegisterResponse;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.Locale;

@Service
public class Register {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        public Register(
                UserRepository userRepository,
                PasswordEncoder passwordEncoder
        ) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        public RegisterResponse register(
                String username,
                String email,
                String password
        ) {

        // Username
        if (username == null || username.trim().isEmpty()) {
                return new RegisterResponse(
                        400,
                        "Username is required"
                );
        }

        username = username.trim();

        if (username.length() < 3) {
                return new RegisterResponse(
                        400,
                        "Username must contain at least 3 characters"
                );
        }

        if (username.length() > 30) {
                return new RegisterResponse(
                        400,
                        "Username cannot contain more than 30 characters"
                );
        }

        // Email
        if (email == null || email.trim().isEmpty()) {
                return new RegisterResponse(
                        400,
                        "Email is required"
                );
        }

        email = email.trim().toLowerCase(Locale.ROOT);

        if (!EMAIL_PATTERN.matcher(email).matches()) {
                return new RegisterResponse(
                        400,
                        "Invalid email address"
                );
        }

        // Password
        if (password == null || password.isEmpty()) {
                return new RegisterResponse(
                        400,
                        "Password is required"
                );
        }

        if (password.length() < 8) {
                return new RegisterResponse(
                        400,
                        "Password must contain at least 8 characters"
                );
        }

        if (password.length() > 72) {
                return new RegisterResponse(
                        400,
                        "Password cannot contain more than 72 characters"
                );
        }

        // Existing username
        if (userRepository.existsByName(username)) {
                return new RegisterResponse(
                        409,
                        "Username is already in use"
                );
        }

        // Existing email
        if (userRepository.existsByEmail(email)) {
                return new RegisterResponse(
                        409,
                        "Email is already in use"
                );
        }

        // Create user
        User user = new User();

        user.setName(username);
        user.setEmail(email);

        // NEVER save plain password
        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole(userRepository.count() == 0 ? "admin" : "user");

        // Save
        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                201,
                "User registered successfully",
                savedUser
        );
    }
}