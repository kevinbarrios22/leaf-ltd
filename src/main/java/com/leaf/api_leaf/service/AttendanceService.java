package com.leaf.api_leaf.service;

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

    public AttendanceRecord registerCheckIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        attendanceRepository.findByEmployeeIdAndDate(employee.getId(), LocalDate.now())
                .ifPresent(r -> {
                    throw new RuntimeException("Already registered today");
                });

        LocalTime now = LocalTime.now();
        LocalTime scheduledStart = LocalTime.of(8, 0);

        AttendanceStatus status = now.isAfter(scheduledStart.plusMinutes(10))
                ? AttendanceStatus.LATE
                : AttendanceStatus.ON_TIME;

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployee(employee);
        record.setDate(LocalDate.now());
        record.setCheckIn(now);
        record.setScheduledStart(scheduledStart);
        record.setScheduledEnd(LocalTime.of(17, 0));
        record.setStatus(status);

        return attendanceRepository.save(record);
    }

    public AttendanceRecord registerCheckOut(Long employeeId) {
        AttendanceRecord record = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("There is no check-in for today "));

        LocalTime now = LocalTime.now();
        record.setCheckOut(now);

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