package com.example.store_service.repositry;

import com.example.store_service.model.StockHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockHistoryRepository extends JpaRepository<StockHistory, UUID> {
    List<StockHistory> findByStockId(UUID stockId);

    Page<StockHistory> findByStockId(UUID stockId, Pageable pageable);
}
