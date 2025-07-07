package com.example.store_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private int storeId;
    private int productId;
    private int quantity;
    private String Reason;
}
