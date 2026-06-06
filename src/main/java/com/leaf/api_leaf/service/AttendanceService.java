package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.AttendanceRequest;
import com.leaf.api_leaf.enums.AttendanceStatus;
import com.leaf.api_leaf.model.AttendanceRecord;
import com.leaf.api_leaf.model.Employee;
import com.leaf.api_leaf.repository.AttendanceRepository;
import com.leaf.api_leaf.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceRecord registerCheckIn(AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Verificar si ya marcó entrada hoy
        attendanceRepository.findByEmployeeIdAndDate(employee.getId(), LocalDate.now())
                .ifPresent(r -> { throw new RuntimeException("Ya registró entrada hoy"); });

        LocalTime now = LocalTime.now();
        LocalTime scheduledStart = request.getScheduledStart() != null
                ? request.getScheduledStart()
                : LocalTime.of(8, 0); // hora default 8:00am

        AttendanceStatus status = now.isAfter(scheduledStart.plusMinutes(10))
                ? AttendanceStatus.LATE
                : AttendanceStatus.ON_TIME;

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployee(employee);
        record.setDate(LocalDate.now());
        record.setCheckIn(now);
        record.setScheduledStart(scheduledStart);
        record.setScheduledEnd(request.getScheduledEnd());
        record.setStatus(status);
        record.setNotes(request.getNotes());

        return attendanceRepository.save(record);
    }

    public AttendanceRecord registerCheckOut(AttendanceRequest request) {
        AttendanceRecord record = attendanceRepository
                .findByEmployeeIdAndDate(request.getEmployeeId(), LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No existe registro de entrada para hoy"));

        LocalTime now = LocalTime.now();
        record.setCheckOut(now);

        // Detectar salida anticipada
        if (record.getScheduledEnd() != null && now.isBefore(record.getScheduledEnd())) {
            record.setStatus(AttendanceStatus.EARLY_LEAVE);
        }

        return attendanceRepository.save(record);
    }

    public List<AttendanceRecord> getAllBetween(LocalDate from, LocalDate to) {
        return attendanceRepository.findByDateBetween(from, to);
    }

    public List<AttendanceRecord> getByEmployeeId(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId);
    }
}