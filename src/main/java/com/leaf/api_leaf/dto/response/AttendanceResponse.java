package com.leaf.api_leaf.dto.response;

import com.leaf.api_leaf.model.AttendanceRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private LocalTime scheduledStart;
    private LocalTime scheduledEnd;
    private String status;
    private String notes;

    public static AttendanceResponse from(AttendanceRecord record) {
        AttendanceResponse dto = new AttendanceResponse();
        dto.setId(record.getId());
        dto.setDate(record.getDate());
        dto.setCheckIn(record.getCheckIn());
        dto.setCheckOut(record.getCheckOut());
        dto.setScheduledStart(record.getScheduledStart());
        dto.setScheduledEnd(record.getScheduledEnd());
        dto.setStatus(record.getStatus() != null ? record.getStatus().name() : null);
        dto.setNotes(record.getNotes());
        if (record.getEmployee() != null) {
            dto.setEmployeeId(record.getEmployee().getId());
            dto.setEmployeeName(record.getEmployee().getFullName());
        }
        return dto;
    }
}