package com.example.store_service.model;

import brave.internal.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Store is required")
    private Long storeId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 0, message = "Quantity must be zero or more")
    private int quantity;
}
