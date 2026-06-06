package com.leaf.api_leaf.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AttendanceRequest {

    @NotNull(message = "El ID del empleado es obligatorio")
    private Long employeeId;

    private LocalTime scheduledStart;
    private LocalTime scheduledEnd;
    private String notes;
}
