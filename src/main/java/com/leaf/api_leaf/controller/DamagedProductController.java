package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.DamagedProductDTO;
import com.leaf.api_leaf.dto.response.ApiResponse;
import com.leaf.api_leaf.dto.response.DamagedProductResponse;
import com.leaf.api_leaf.service.DamagedProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "STORE", description = "Damaged products registry")
@RestController
@RequestMapping("/api/damaged")
@RequiredArgsConstructor
public class DamagedProductController {

    private final DamagedProductService damagedProductService;

    @Operation(summary = "Register damaged product")
    @PostMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER','EMPLOYEE')")
    public ResponseEntity<DamagedProductResponse> create(
            @Valid @RequestBody DamagedProductDTO dto,
            Authentication authentication) {
        return ResponseEntity.ok(DamagedProductResponse.from(
                damagedProductService.create(dto, authentication.getName())));
    }

    @Operation(summary = "List damaged products", description = "Optionally filter by date or item number")
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<ApiResponse<Page<DamagedProductResponse>>> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String itemNumber,
            @PageableDefault(size = 10, sort = "reportDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(damagedProductService.getAll(date, itemNumber, pageable)
                        .map(DamagedProductResponse::from)));
    }

    @Operation(summary = "Get damaged product by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<DamagedProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                DamagedProductResponse.from(damagedProductService.getById(id)));
    }

    @Operation(summary = "Update damaged product")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<DamagedProductResponse> update(
            @PathVariable Long id, @Valid @RequestBody DamagedProductDTO dto) {
        return ResponseEntity.ok(
                DamagedProductResponse.from(damagedProductService.update(id, dto)));
    }

    @Operation(summary = "Delete damaged product record", description = "Only BOSS can delete")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damagedProductService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
