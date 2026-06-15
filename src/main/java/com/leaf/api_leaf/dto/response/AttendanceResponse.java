package com.leaf.api_leaf.dto.response;

import com.leaf.api_leaf.model.AttendanceRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceResponse {

    private Long employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime entryTime;
    private LocalTime exitTime;
    private String status;

    public static AttendanceResponse from(AttendanceRecord record) {
        AttendanceResponse dto = new AttendanceResponse();
        dto.setDate(record.getDate());
        dto.setEntryTime(record.getEntryTime());
        dto.setExitTime(record.getExitTime());
        dto.setStatus(record.getStatus());
        if (record.getEmployee() != null) {
            dto.setEmployeeId(record.getEmployee().getId());
            dto.setEmployeeName(record.getEmployee().getFullName());
        }
        return dto;
    }
}
