package com.leaf.api_leaf.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AttendanceRequest {

    @NotNull(message = "The employee ID is required")
    private Long employeeId;

    private LocalTime scheduledStart;
    private LocalTime scheduledEnd;
    private String notes;
}
