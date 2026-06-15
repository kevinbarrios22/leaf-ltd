package com.leaf.api_leaf.controller;

import com.leaf.api_leaf.dto.request.BatchAttendanceRequest;
import com.leaf.api_leaf.dto.response.ApiResponse;
import com.leaf.api_leaf.dto.response.AttendanceResponse;
import com.leaf.api_leaf.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Attendance", description = "Employee attendance and schedule control")
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "Batch save attendance", description = "Save multiple attendance records for a date")
    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> batchSave(
            @Valid @RequestBody BatchAttendanceRequest request) {
        int saved = attendanceService.saveBatch(request);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("saved", saved)));
    }

    @Operation(summary = "Get attendance by date")
    @GetMapping("/by-date")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE','MANAGER','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceResponse> records = attendanceService.getByDate(date).stream()
                .map(AttendanceResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(records));
    }

    @Operation(summary = "Get monthly attendance report", description = "Grid of employees x days")
    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('BOSS','OFFICE')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReport(
            @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.ok(attendanceService.getMonthlyReport(year, month)));
    }
}
