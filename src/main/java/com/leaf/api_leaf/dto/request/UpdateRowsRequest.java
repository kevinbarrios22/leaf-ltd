package com.leaf.api_leaf.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRowsRequest {

    private List<RowUpdate> rows;

    @Data
    public static class RowUpdate {
        private String customer;
        private int quantity;
        private boolean delivered;
    }
}
