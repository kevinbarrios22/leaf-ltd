package com.leaf.api_leaf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DeliverySheetDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate deliveryDate;

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    private String storeName;

    @NotBlank(message = "El nombre del repartidor es obligatorio")
    private String driverName;

    @NotBlank(message = "La placa es obligatoria")
    private String vehiclePlate;

    private List<DeliveryInvoiceDTO> invoices;


}
