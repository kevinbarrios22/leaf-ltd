package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.DamagedProductDTO;
import com.leaf.api_leaf.model.DamagedProduct;
import com.leaf.api_leaf.service.DamagedProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/damaged")
@RequiredArgsConstructor
public class DamagedProductController {

    private final DamagedProductService damagedProductService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JEFE','BODEGA')")
    public ResponseEntity<DamagedProduct> create(
            @Valid @RequestBody DamagedProductDTO dto,
            Authentication authentication) {
        return ResponseEntity.ok(
                damagedProductService.create(dto, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('JEFE','BODEGA')")
    public ResponseEntity<List<DamagedProduct>> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String itemNumber) {
        return ResponseEntity.ok(damagedProductService.getAll(date, itemNumber));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE','BODEGA')")
    public ResponseEntity<DamagedProduct> getById(@PathVariable Long id) {
        return ResponseEntity.ok(damagedProductService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damagedProductService.delete(id);
        return ResponseEntity.noContent().build();
    }
}