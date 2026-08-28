package com.example.app.dto;

import com.example.app.model.User;

public class UserServiceResponse {

    private int status;
    private String message;
    private User user;

    public UserServiceResponse(
            int status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    public UserServiceResponse(
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