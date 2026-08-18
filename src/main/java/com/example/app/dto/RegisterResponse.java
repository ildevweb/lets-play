package com.example.app.dto;

public class RegisterResponse {

    private int status;
    private String message;
    private Object data;

    public RegisterResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public RegisterResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}