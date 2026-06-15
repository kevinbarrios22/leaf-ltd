package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.EmployeeDTO;
import com.leaf.api_leaf.dto.response.ApiResponse;
import com.leaf.api_leaf.dto.response.EmployeeResponse;
import com.leaf.api_leaf.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Employees", description = "Employees management")
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create employee")
    @PostMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.create(dto)));
    }




    @Operation(summary = "List all active employees")
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','OFFICE')")
    public ResponseEntity<ApiResponse<Page<EmployeeResponse>>> getAll(
            @PageableDefault(size = 10, sort = "fullName", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(employeeService.getAll(pageable)
                        .map(EmployeeResponse::from)));
    }




    @Operation(summary = "Get employee by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.getById(id)));
    }

    @Operation(summary = "Update employee")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER')")
    public ResponseEntity<EmployeeResponse> update(
            @PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.update(id, dto)));
    }

    @Operation(summary = "Deactivate employee")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}