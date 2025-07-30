package com.example.store_service.service;

import com.example.store_service.model.StockHistory;
import com.example.store_service.repositry.StockHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class StockHistoryService {
    private final StockHistoryRepository stockHistoryRepository;
    @Autowired
    public StockHistoryService(StockHistoryRepository stockHistoryRepository) {
        this.stockHistoryRepository = stockHistoryRepository;
    }


    public List<StockHistory> getStockHistoryForStore(UUID storeId) {
        log.info("Fetching History");
        return stockHistoryRepository.findByStoreId(storeId);
    }

    public Page<StockHistory> getStockHistoryForStorePaginated(UUID stockId, Pageable pageable) {
        log.info("Fetching history for stock: {} with pagination - Page: {}, Size: {}, Sort: {}",
                stockId, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return stockHistoryRepository.findByStockId(stockId, pageable);
    }
}
