package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.DeliverySheetDTO;
import com.leaf.api_leaf.model.DeliverySheet;
import com.leaf.api_leaf.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BOSS','STORE','OFFICE', 'MANAGER')")
    public ResponseEntity<DeliverySheet> create(
            @Valid @RequestBody DeliverySheetDTO dto) {
        return ResponseEntity.ok(deliveryService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','STORE','OFFICE', 'MANAGER')")
    public ResponseEntity<List<DeliverySheet>> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(deliveryService.getAll(date));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','STORE','OFFICE', 'MANAGER')")
    public ResponseEntity<DeliverySheet> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','STORE','OFFICE', 'MANAGER')")
    public ResponseEntity<DeliverySheet> update(
            @PathVariable Long id,
            @Valid @RequestBody DeliverySheetDTO dto) {
        return ResponseEntity.ok(deliveryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('JEFE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}