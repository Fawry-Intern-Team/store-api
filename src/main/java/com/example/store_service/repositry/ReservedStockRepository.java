package com.example.store_service.repositry;

import com.example.store_service.model.ReservedStock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservedStockRepository extends JpaRepository<ReservedStock,UUID> {
    Pageable pageable = PageRequest.of(0, 10);

    List<ReservedStock> findByOrderId(UUID orderId);

    void deleteByOrderId(UUID testOrderId);
}
