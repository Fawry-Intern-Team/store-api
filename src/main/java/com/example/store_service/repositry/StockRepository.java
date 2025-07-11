package com.example.store_service.repositry;

import com.example.store_service.model.Stock;
import com.example.store_service.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {
    Optional<Stock> findByStoreIdAndProductId(Long storeId, Long productId);

    Optional<Stock> findByProductId(Long productId);
}
