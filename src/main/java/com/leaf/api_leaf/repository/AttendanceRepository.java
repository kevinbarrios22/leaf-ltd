package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByEmployeeId(Long employeeId);
    List<AttendanceRecord> findByDateBetween(LocalDate from, LocalDate to);
    Optional<AttendanceRecord> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}
