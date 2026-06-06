package com.leaf.api_leaf.dto.response;

import com.leaf.api_leaf.model.DeliveryInvoice;
import com.leaf.api_leaf.model.DeliverySheet;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DeliverySheetResponse {
    private Long id;
    private LocalDate deliveryDate;
    private String storeName;
    private String driverName;
    private String vehiclePlate;
    private List<InvoiceResponse> invoices;
    private int totalInvoices;

    @Getter
    @Setter
    public static class InvoiceResponse {
        private Long id;
        private String customerName;
        private String invoiceNumber;
        private Integer invoiceCount;

        public static InvoiceResponse from(DeliveryInvoice inv) {
            InvoiceResponse dto = new InvoiceResponse();
            dto.setId(inv.getId());
            dto.setCustomerName(inv.getCustomerName());
            dto.setInvoiceNumber(inv.getInvoiceNumber());
            dto.setInvoiceCount(inv.getInvoiceCount());
            return dto;
        }
    }

    public static DeliverySheetResponse from(DeliverySheet sheet) {
        DeliverySheetResponse dto = new DeliverySheetResponse();
        dto.setId(sheet.getId());
        dto.setDeliveryDate(sheet.getDeliveryDate());
        dto.setStoreName(sheet.getStoreName());
        dto.setDriverName(sheet.getDriverName());
        dto.setVehiclePlate(sheet.getVehiclePlate());
        dto.setInvoices(sheet.getInvoices().stream()
                .map(InvoiceResponse::from)
                .collect(Collectors.toList()));
        dto.setTotalInvoices(sheet.getInvoices().size());
        return dto;
    }
}