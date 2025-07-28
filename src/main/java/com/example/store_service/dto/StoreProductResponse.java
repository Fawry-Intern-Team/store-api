package com.example.store_service.dto;

import com.example.store_service.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductResponse {
    private UUID productId;
    private Store store;
}