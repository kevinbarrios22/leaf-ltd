package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.AuthResponse;
import com.leaf.api_leaf.dto.LoginRequest;
import com.leaf.api_leaf.dto.RegisterRequest;
import com.leaf.api_leaf.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Solo el JEFE puede registrar nuevos usuarios
    @PostMapping("/register")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}