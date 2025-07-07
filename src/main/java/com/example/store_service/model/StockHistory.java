package com.example.store_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    private Product product;

    @NotBlank
    @ManyToOne(optional = false)
    @JoinColumn(name ="store_id")
    private Store store;

    @NotBlank
    private int quantityChange;

    @NotBlank
    private String reason;

    @NotBlank
    private LocalDateTime timestamp;


}
