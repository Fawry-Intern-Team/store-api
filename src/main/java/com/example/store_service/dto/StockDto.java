package com.example.store_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private Long storeId;
    private Long productId;
    private int quantity;
    private String Reason;
}
