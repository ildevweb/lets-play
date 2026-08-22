package com.example.app.dto;

public class LoginResponse {

    private int status;
    private String message;
    private String token;

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
            String token
    ) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}