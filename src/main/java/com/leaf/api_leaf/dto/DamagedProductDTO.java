package com.leaf.api_leaf.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DamagedProductDTO {


    @NotNull(message = "La fecha es obligatoria")
    private LocalDate reportDate;

    @NotBlank(message = "El número de ítem es obligatorio")
    private String itemNumber;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1 ")
    private Integer quantity;

    private String description;

}
