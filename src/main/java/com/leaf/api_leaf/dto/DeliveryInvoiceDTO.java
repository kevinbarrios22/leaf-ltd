package com.leaf.api_leaf.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeliveryInvoiceDTO {

    @NotBlank(message = "The customer name is required")
    private String customerName;


    private String invoiceNumber;
    @NotBlank(message = "The quantity of invoices is Required")
    private Integer invoiceCount;

}
