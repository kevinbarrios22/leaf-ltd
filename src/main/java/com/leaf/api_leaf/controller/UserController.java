package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.response.ApiResponse;
import com.leaf.api_leaf.dto.response.UserResponse;
import com.leaf.api_leaf.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Users", description = "System user management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "List all users", description = "Only BOSS and OFFICE can access this endpoint")
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAll(
            @PageableDefault(size = 10, sort = "username", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(userService.getAll(pageable)
                        .map(UserResponse::from)));
    }

    @Operation(summary = "Toggle user enabled/disabled")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE')")
    public ResponseEntity<ApiResponse<Map<String, String>>> toggleEnabled(@PathVariable Long id) {
        userService.toggleEnabled(id);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "User updated")));
    }

    @Operation(summary = "Delete user", description = "Only BOSS can delete")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
