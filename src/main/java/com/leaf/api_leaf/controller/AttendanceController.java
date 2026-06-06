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

@Tag(name = "Attendance", description = "Employee attendance and schedule control")
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "Register check-in", description = "Records employee arrival time")
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<AttendanceResponse> checkIn(@PathVariable Long employeeId) {
        return ResponseEntity.ok(
                AttendanceResponse.from(attendanceService.registerCheckIn(employeeId)));
    }

    @Operation(summary = "Register chek-out", description = "Records employee departure time")
    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<AttendanceResponse> checkOut(@PathVariable Long employeeId) {
        return ResponseEntity.ok(
                AttendanceResponse.from(attendanceService.registerCheckOut(employeeId)));
    }

    @Operation(summary = "Get attendance by date range", description = "Filter attendance records between two dates")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('BOSS','EMPLOYEE')")
    public ResponseEntity<List<AttendanceResponse>> getAll(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(attendanceService.getAllBetween(from, to).stream()
                .map(AttendanceResponse::from).collect(Collectors.toList()));
    }

    @Operation(summary = "Get attendance by employee")
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('BOSS','EMPLOYEE')")
    public ResponseEntity<List<AttendanceResponse>> getByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getByEmployeeId(employeeId).stream()
                .map(AttendanceResponse::from).collect(Collectors.toList()));
    }
}