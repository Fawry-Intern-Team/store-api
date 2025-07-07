package com.example.store_service.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservedStock {
    @Id
    private Long id;

    private Long order_id;
    private Long product_id;
    private int quantity;
}
