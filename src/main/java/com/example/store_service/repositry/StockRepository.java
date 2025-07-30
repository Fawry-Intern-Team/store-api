package com.example.store_service.repositry;

import com.example.store_service.model.Stock;
import com.example.store_service.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock,UUID> {
    Optional<Stock> findByStoreIdAndProductId(UUID storeId, UUID productId);

    Optional<Stock> findByProductId(UUID productId);

    List<Stock> findStocksByProductId(UUID productId);

    Optional<List<Stock>> findByStoreId(UUID storeId);

    Page<Stock> findByStoreId(UUID storeId, Pageable pageable);
}
