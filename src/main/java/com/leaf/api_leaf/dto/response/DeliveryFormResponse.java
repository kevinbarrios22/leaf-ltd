package com.leaf.api_leaf.dto.response;

import com.leaf.api_leaf.model.DeliveryForm;
import com.leaf.api_leaf.model.DeliveryFormRow;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DeliveryFormResponse {

    private Long id;
    private LocalDate date;
    private String person;
    private String driver;
    private String plate;
    private LocalTime time;
    private LocalDate deliveryDate;
    private int totalQty;
    private int deliveredQty;
    private List<RowResponse> rows;

    @Getter
    @Setter
    public static class RowResponse {
        private String customer;
        private int quantity;
        private boolean delivered;

        public static RowResponse from(DeliveryFormRow row) {
            RowResponse dto = new RowResponse();
            dto.setCustomer(row.getCustomer());
            dto.setQuantity(row.getQuantity());
            dto.setDelivered(row.isDelivered());
            return dto;
        }
    }

    public static DeliveryFormResponse from(DeliveryForm form) {
        DeliveryFormResponse dto = new DeliveryFormResponse();
        dto.setId(form.getId());
        dto.setDate(form.getDate());
        dto.setPerson(form.getPerson());
        dto.setDriver(form.getDriver());
        dto.setPlate(form.getPlate());
        dto.setTime(form.getTime());
        dto.setDeliveryDate(form.getDeliveryDate());

        List<RowResponse> rowResponses = form.getRows().stream()
                .map(RowResponse::from)
                .collect(Collectors.toList());
        dto.setRows(rowResponses);
        dto.setTotalQty(rowResponses.stream().mapToInt(RowResponse::getQuantity).sum());
        dto.setDeliveredQty(rowResponses.stream().filter(RowResponse::isDelivered)
                .mapToInt(RowResponse::getQuantity).sum());

        return dto;
    }
}
