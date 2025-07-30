package com.example.store_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String location;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Stock> stocks;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<StockHistory> stockHistories;
}
