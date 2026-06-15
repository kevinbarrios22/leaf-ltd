package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.request.BatchAttendanceRequest;
import com.leaf.api_leaf.model.AttendanceRecord;
import com.leaf.api_leaf.model.Employee;
import com.leaf.api_leaf.repository.AttendanceRepository;
import com.leaf.api_leaf.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public int saveBatch(BatchAttendanceRequest request) {
        int saved = 0;
        for (BatchAttendanceRequest.RecordDTO recordDTO : request.getRecords()) {
            Employee employee = employeeRepository.findById(recordDTO.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + recordDTO.getEmployeeId()));

            AttendanceRecord record = attendanceRepository
                    .findByEmployeeIdAndDate(recordDTO.getEmployeeId(), request.getDate())
                    .orElseGet(AttendanceRecord::new);

            record.setEmployee(employee);
            record.setDate(request.getDate());
            record.setEntryTime(recordDTO.getEntryTime());
            record.setExitTime(recordDTO.getExitTime());
            record.setStatus(recordDTO.getStatus());

            attendanceRepository.save(record);
            saved++;
        }
        return saved;
    }

    public List<AttendanceRecord> getByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public Map<String, Object> getMonthlyReport(int year, int month) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());

        List<Employee> employees = employeeRepository.findByActiveTrue(Pageable.unpaged()).getContent();
        List<AttendanceRecord> records = attendanceRepository.findByDateBetween(from, to);

        Map<Long, Map<Integer, Map<String, Object>>> employeeRecords = new LinkedHashMap<>();

        for (Employee emp : employees) {
            Map<Integer, Map<String, Object>> days = new LinkedHashMap<>();
            for (int day = 1; day <= from.lengthOfMonth(); day++) {
                Map<String, Object> dayRecord = new HashMap<>();
                dayRecord.put("status", "");
                dayRecord.put("entryTime", null);
                dayRecord.put("exitTime", null);
                days.put(day, dayRecord);
            }
            employeeRecords.put(emp.getId(), days);
        }

        for (AttendanceRecord rec : records) {
            int day = rec.getDate().getDayOfMonth();
            Map<String, Object> dayRecord = employeeRecords.get(rec.getEmployee().getId()).get(day);
            dayRecord.put("status", rec.getStatus());
            dayRecord.put("entryTime", rec.getEntryTime());
            dayRecord.put("exitTime", rec.getExitTime());
        }

        List<Integer> daysList = new ArrayList<>();
        for (int day = 1; day <= from.lengthOfMonth(); day++) {
            daysList.add(day);
        }

        List<Map<String, Object>> employeesList = new ArrayList<>();
        for (Employee emp : employees) {
            Map<String, Object> empMap = new LinkedHashMap<>();
            empMap.put("employeeId", emp.getId());
            empMap.put("name", emp.getFullName());
            empMap.put("records", employeeRecords.get(emp.getId()));
            employeesList.add(empMap);
        }

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("days", daysList);
        report.put("employees", employeesList);

        return report;
    }
}
