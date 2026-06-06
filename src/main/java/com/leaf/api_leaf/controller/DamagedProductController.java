package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.DamagedProductDTO;
import com.leaf.api_leaf.dto.response.DamagedProductResponse;
import com.leaf.api_leaf.service.DamagedProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Warehouse", description = "Damaget products registry")
@RestController
@RequestMapping("/api/damaged")
@RequiredArgsConstructor
public class DamagedProductController {

    private final DamagedProductService damagedProductService;

    @Operation(summary = "Register damaged prodcut ")
    @PostMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER','EMPLOYEE')")
    public ResponseEntity<DamagedProductResponse> create(
            @Valid @RequestBody DamagedProductDTO dto,
            Authentication authentication) {
        return ResponseEntity.ok(DamagedProductResponse.from(
                damagedProductService.create(dto, authentication.getName())));
    }

    @Operation(summary = "List damaged products ", description = "Optionally filter by date  or item number")
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<List<DamagedProductResponse>> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String itemNumber) {
        return ResponseEntity.ok(damagedProductService.getAll(date, itemNumber).stream()
                .map(DamagedProductResponse::from).collect(Collectors.toList()));
    }

    @Operation(summary = "Get damaged product by ID ")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<DamagedProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                DamagedProductResponse.from(damagedProductService.getById(id)));
    }

    @Operation(summary = "Delete damaged product record", description = "Only BOSS can delete ")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damagedProductService.delete(id);
        return ResponseEntity.noContent().build();
    }
}  