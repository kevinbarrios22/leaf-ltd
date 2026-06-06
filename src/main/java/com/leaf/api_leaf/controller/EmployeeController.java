package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.EmployeeDTO;
import com.leaf.api_leaf.dto.response.EmployeeResponse;
import com.leaf.api_leaf.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Employees", description = "Employees management")
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create employee")
    @PostMapping
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.create(dto)));
    }

    @Operation(summary = "List all active employees")
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        return ResponseEntity.ok(employeeService.getAll().stream()
                .map(EmployeeResponse::from).collect(Collectors.toList()));
    }

    @Operation(summary = "Get employee by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFCIE','MANAGER')")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.getById(id)));
    }

    @Operation(summary = "Update employee")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<EmployeeResponse> update(
            @PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.update(id, dto)));
    }

    @Operation(summary = "Desactive employee")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}