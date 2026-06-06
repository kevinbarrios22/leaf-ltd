package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.response.AttendanceResponse;
import com.leaf.api_leaf.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Asistencia", description = "Control de horarios y marcaciones")
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "Marcar entrada del empleado")
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<AttendanceResponse> checkIn(@PathVariable Long employeeId) {
        return ResponseEntity.ok(
                AttendanceResponse.from(attendanceService.registerCheckIn(employeeId)));
    }

    @Operation(summary = "Marcar salida del empleado")
    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<AttendanceResponse> checkOut(@PathVariable Long employeeId) {
        return ResponseEntity.ok(
                AttendanceResponse.from(attendanceService.registerCheckOut(employeeId)));
    }

    @Operation(summary = "Ver asistencia por rango de fechas")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('BOSS','STORE')")
    public ResponseEntity<List<AttendanceResponse>> getAll(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(attendanceService.getAllBetween(from, to).stream()
                .map(AttendanceResponse::from).collect(Collectors.toList()));
    }

    @Operation(summary = "Ver asistencia de un empleado")
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('BOSS','STORE')")
    public ResponseEntity<List<AttendanceResponse>> getByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getByEmployeeId(employeeId).stream()
                .map(AttendanceResponse::from).collect(Collectors.toList()));
    }
}