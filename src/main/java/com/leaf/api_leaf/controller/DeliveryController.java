package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.DeliverySheetDTO;
import com.leaf.api_leaf.dto.response.ApiResponse;
import com.leaf.api_leaf.dto.response.DeliverySheetResponse;
import com.leaf.api_leaf.service.DeliveryService;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Delivery", description = "Delivery sheet management")
@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Create  delivery sheet ")
    @PostMapping
    @PreAuthorize("hasAnyRole('BOSS','MANAGER', 'EMPLOYEE')")
    public ResponseEntity<DeliverySheetResponse> create(
            @Valid @RequestBody DeliverySheetDTO dto) {
        return ResponseEntity.ok(DeliverySheetResponse.from(deliveryService.create(dto)));
    }

    @Operation(summary = "List delivery sheets ", description = "Optionally filter by date")
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','MANAGER','OFFICE','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Page<DeliverySheetResponse>>> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 10, sort = "deliveryDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(deliveryService.getAll(date, pageable)
                        .map(DeliverySheetResponse::from)));
    }

    @Operation(summary = "Get delivery sheet by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<DeliverySheetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(DeliverySheetResponse.from(deliveryService.getById(id)));
    }

    @Operation(summary = "Update delivery sheet ")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','MANAGER')")
    public ResponseEntity<DeliverySheetResponse> update(
            @PathVariable Long id, @Valid @RequestBody DeliverySheetDTO dto) {
        return ResponseEntity.ok(DeliverySheetResponse.from(deliveryService.update(id, dto)));
    }

    @Operation(summary = "Delete delivery sheet", description = "Only BOSS can delete ")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}