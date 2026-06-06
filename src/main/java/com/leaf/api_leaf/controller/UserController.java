package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.response.UserResponse;
import com.leaf.api_leaf.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "Listar todos los usuarios — solo BOSS")
    @GetMapping
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .map(UserResponse::from).collect(Collectors.toList()));
    }
}