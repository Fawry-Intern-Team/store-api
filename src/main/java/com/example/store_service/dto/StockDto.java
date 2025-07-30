package com.example.store_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private  UUID stockId;
    private UUID storeId;
    private UUID productId;
    private int quantity;
    private String Reason;
}
