package com.example.store_service.repositry;

import com.example.store_service.model.Stock;
import com.example.store_service.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock,UUID> {
    Optional<Stock> findByStoreIdAndProductId(UUID storeId, UUID productId);

    Optional<Stock> findByProductId(UUID productId);

    Optional<Stock> findByStoreId(UUID storeId);
}
