package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.request.DeliveryFormRequest;
import com.leaf.api_leaf.dto.request.UpdateRowsRequest;
import com.leaf.api_leaf.dto.response.ApiResponse;
import com.leaf.api_leaf.dto.response.DeliveryFormResponse;
import com.leaf.api_leaf.service.DeliveryFormService;
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
import java.util.Map;

@Tag(name = "Delivery Forms", description = "Delivery forms management")
@RestController
@RequestMapping("/api/delivery-forms")
@RequiredArgsConstructor
public class DeliveryFormController {

    private final DeliveryFormService deliveryFormService;

    @Operation(summary = "Create delivery form")
    @PostMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> create(@Valid @RequestBody DeliveryFormRequest request) {
        DeliveryFormResponse response = DeliveryFormResponse.from(deliveryFormService.create(request));
        return ResponseEntity.ok(ApiResponse.ok(Map.of("id", response.getId())));
    }

    @Operation(summary = "List delivery forms", description = "Filter by date, customer or driver")
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<ApiResponse<Page<DeliveryFormResponse>>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String driver,
            @PageableDefault(size = 15, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(deliveryFormService.getAll(date, customer, driver, pageable)
                        .map(DeliveryFormResponse::from)));
    }

    @Operation(summary = "Update delivery form rows (delivered status)")
    @PutMapping("/{id}/rows")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateRows(
            @PathVariable Long id, @RequestBody UpdateRowsRequest request) {
        deliveryFormService.updateRows(id, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "Delete delivery form")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryFormService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
