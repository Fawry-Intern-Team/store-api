package com.example.store_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Store is required")
    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    // Consider allowing negative values for decrease
    private int quantityChange;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
}
