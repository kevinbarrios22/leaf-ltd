package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<List<AppUser>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}