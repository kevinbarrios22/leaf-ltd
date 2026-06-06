package com.leaf.api_leaf.dto.response;

import com.leaf.api_leaf.model.DamagedProduct;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DamagedProductResponse {
    private Long id;
    private LocalDate reportDate;
    private String itemNumber;
    private Integer quantity;
    private String description;
    private String reportedBy;

    public static DamagedProductResponse from(DamagedProduct product) {
        DamagedProductResponse dto = new DamagedProductResponse();
        dto.setId(product.getId());
        dto.setReportDate(product.getReportDate());
        dto.setItemNumber(product.getItemNumber());
        dto.setQuantity(product.getQuantity());
        dto.setDescription(product.getDescription());
        if (product.getReportedBy() != null) {
            dto.setReportedBy(product.getReportedBy().getUsername());
        }
        return dto;
    }
}