package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.response.AuthResponse;
import com.leaf.api_leaf.dto.request.LoginRequest;
import com.leaf.api_leaf.dto.request.RegisterRequest;
import com.leaf.api_leaf.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Login and registration")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login", description = "Authenticates user and returns a JWT token valid for 24 hours")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Register user", description = "Only BOSS role can register new users")
    @PostMapping("/register")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}