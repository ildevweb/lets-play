package com.example.app.service;

import com.example.app.dto.LoginResponse;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class Login {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Login(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(
        String email,
        String password
    ) {

        // Email
        if (email == null || email.trim().isEmpty()) {
            return new LoginResponse(
                400,
                "Email is required"
            );
        }

        email = email.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new LoginResponse(
                400,
                "Invalid email address"
            );
        }

        // Password
        if (password == null || password.isEmpty()) {
            return new LoginResponse(
                400,
                "Password is required"
            );
        }

        // Find user
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return new LoginResponse(
                401,
                "Invalid email or password"
            );
        }

        // Check password
        if (!passwordEncoder.matches(
            password,
            user.getPassword()
        )) {
            return new LoginResponse(
                401,
                "Invalid email or password"
            );
        }

        // Generate JWT
        String token = jwtService.generateToken(user);

        
        // Login successful
        return new LoginResponse(
            200,
            "Login successful",
            token
        );
    }
}