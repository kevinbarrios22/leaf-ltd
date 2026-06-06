package com.leaf.api_leaf.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DamagedProductDTO {


    @NotNull(message = "The date is required")
    private LocalDate reportDate;

    @NotBlank(message = "The item code is required")
    private String itemNumber;

    @NotNull(message = "The quantity is required")
    @Min(value = 1, message = "The quantity must be at least 1 ")
    private Integer quantity;

    private String description;

}
