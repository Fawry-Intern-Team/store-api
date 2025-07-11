package com.example.store_service.repositry;

import com.example.store_service.model.ReservedStock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedStockRepository extends JpaRepository<ReservedStock,Integer> {
    Pageable pageable = PageRequest.of(0, 10);
    List<ReservedStock> findByOrderId(Long orderId);

    void deleteByOrderId(Long testOrderId);
}
