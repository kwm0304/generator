package com.kwm0304.cli.entity;

public class AuthResponse {
    private String token;
    private String message;
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getToken() { return token; }
    public String getMessage() { return message; }
}