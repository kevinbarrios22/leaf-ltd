package com.leaf.api_leaf.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class DeliveryFormRequest {

    @NotNull
    private LocalDate date;

    @NotBlank
    private String person;

    @NotBlank
    private String driver;

    @NotBlank
    private String plate;

    private LocalTime time;

    @NotNull
    private LocalDate deliveryDate;

    private List<RowRequest> rows;

    @Data
    public static class RowRequest {
        @NotBlank
        private String customer;
        private int quantity;
    }
}
