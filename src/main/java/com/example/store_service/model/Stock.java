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

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "storeId", nullable = false)
    private Store store;

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Min(value = 0, message = "Quantity must be zero or more")
    private int quantity;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL)
    private List<StockHistory> historyList;
}
