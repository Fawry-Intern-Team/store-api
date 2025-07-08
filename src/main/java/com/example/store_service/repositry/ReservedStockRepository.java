package com.example.store_service.repositry;

import com.example.store_service.model.ReservedStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedStockRepository extends JpaRepository<ReservedStock,Integer> {
    List<ReservedStock> findByOrderId(Long orderId);


}
