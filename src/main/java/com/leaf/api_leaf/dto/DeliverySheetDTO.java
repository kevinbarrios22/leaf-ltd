package com.leaf.api_leaf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DeliverySheetDTO {

    @NotNull(message = "The date is required")
    private LocalDate deliveryDate;

    @NotBlank(message = "The store name is required")
    private String storeName;

    @NotBlank(message = "The driver name is required")
    private String driverName;

    @NotBlank(message = "The plate is required")
    private String vehiclePlate;

    private List<DeliveryInvoiceDTO> invoices;


}
