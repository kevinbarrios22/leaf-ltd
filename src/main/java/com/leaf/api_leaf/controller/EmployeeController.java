package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.model.Employee;
import com.leaf.api_leaf.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Crear empleado — solo BOSS
    @PostMapping
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Employee> create(@Valid @RequestBodyEmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.create(dto));
    }

    // Ver todos — BOSS y STORE
    @GetMapping
    @PreAuthorize("hasAnyRole('BOSS','STORE')")
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    // Ver uno por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('BOSS','STORE')")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    // Editar empleado — solo BOSS
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Employee> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.update(id, dto));
    }

    // Desactivar empleado — solo BOSS
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BOSS')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}