package com.example.app.controller;

import com.example.app.dto.RegisterResponse;
import com.example.app.dto.LoginRequest;
import com.example.app.dto.LoginResponse;
import com.example.app.model.User;
import com.example.app.service.Register;
import com.example.app.service.Login;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final Register register;
    private final Login login;

    public AuthController(Register register, Login login) {
        this.register = register;
        this.login = login;
    }

    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody User user) {
        
        RegisterResponse response = register.register(
            user.getName(),
            user.getEmail(),
            user.getPassword()
        );

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = login.login(
            request.getEmail(),
            request.getPassword()
        );

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }
}
