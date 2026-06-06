package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.AttendanceRequest;
import com.leaf.api_leaf.model.AttendanceRecord;
import com.leaf.api_leaf.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // Cualquier usuario autenticado puede marcar entrada
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceRecord> checkIn(
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.registerCheckIn(request));
    }

    // Cualquier usuario autenticado puede marcar salida
    @PostMapping("/check-out")
    public ResponseEntity<AttendanceRecord> checkOut(
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.registerCheckOut(request));
    }

    // Solo JEFE y OFICINA pueden ver la asistencia de todos
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('JEFE','OFICINA')")
    public ResponseEntity<List<AttendanceRecord>> getAll(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(attendanceService.getAllBetween(from, to));
    }

    // Ver asistencia por empleado
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('JEFE','OFICINA')")
    public ResponseEntity<List<AttendanceRecord>> getByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getByEmployeeId(employeeId));
    }
}