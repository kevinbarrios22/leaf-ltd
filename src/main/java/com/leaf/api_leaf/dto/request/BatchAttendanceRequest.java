package com.leaf.api_leaf.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BatchAttendanceRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private List<RecordDTO> records;

    @Data
    public static class RecordDTO {
        @NotNull
        private Long employeeId;
        private LocalTime entryTime;
        private LocalTime exitTime;

        @NotNull
        private String status;
    }
}
