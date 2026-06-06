package com.leaf.api_leaf.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeliveryInvoiceDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String customerName;

    @NotBlank(message = "El numero  de invoice es obligatorio")
    private String invoiceNumber;

    private Integer invoiceCount;

}
