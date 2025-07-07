package com.example.store_service.service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.repositry.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StockService {


    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    public void addStock(StockDto stockDto) {
        Store store = storeRepository.findById(stockDto.getStoreId()).orElseThrow();
        Stock stock1 = stockRepository.findByStoreAndProductId(store,stockDto.getProductId()).orElseThrow();
        stock1.setQuantity(stock1.getQuantity() + stockDto.getQuantity());
        stockRepository.save(stock1);
        stockHistoryRepository.save(
                StockHistory.builder()
                        .store(store)
                        .quantityChange(stockDto.getQuantity())
                        .reason(stockDto.getReason())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    public void consumeStock(StockDto stockDto) {
        Store store = storeRepository.findById(stockDto.getStoreId()).orElseThrow();
        Stock stock = stockRepository.findByStoreAndProductId(store, stockDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        if (stock.getQuantity() < stockDto.getQuantity())
            throw new RuntimeException("Not enough stock");
        stock.setQuantity(stock.getQuantity()-stockDto.getQuantity());
        stockRepository.save(stock);
        stockHistoryRepository.save(
                StockHistory.builder()
                        .store(store)
                        .quantityChange(stockDto.getQuantity())
                        .reason(stockDto.getReason())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
