package com.kwm0304.cli.controller;

import com.kwm0304.cli.entity.AuthResponse;
import com.kwm0304.cli.entity.Customer;
import com.kwm0304.cli.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Customer request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Customer request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}