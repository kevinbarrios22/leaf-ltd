package com.leaf.api_leaf.service;

import com.leaf.api_leaf.repository.AttendanceRepository;
import com.leaf.api_leaf.repository.DamagedProductRepository;
import com.leaf.api_leaf.repository.EmployeeRepository;
import com.leaf.api_leaf.repository.DeliveryFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final DeliveryFormRepository deliveryFormRepository;
    private final DamagedProductRepository damagedProductRepository;

    public Map<String, Object> getStats() {
        LocalDate today = LocalDate.now();

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeEmployees", employeeRepository.countByActiveTrue());
        stats.put("todayAttendance", attendanceRepository.countByDate(today));
        stats.put("todayDeliveries", deliveryFormRepository.countByDate(today));
        stats.put("todayDamaged", damagedProductRepository.countByReportDate(today));

        return stats;
    }
}
