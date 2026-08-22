package com.example.app.dto;

import com.example.app.model.User;

public class LoginResponse {

    private int status;
    private String message;
    private User user;

    public LoginResponse(
            int status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    public LoginResponse(
            int status,
            String message,
            User user
    ) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}