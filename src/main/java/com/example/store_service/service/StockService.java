package com.example.store_service.service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.repositry.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class StockService {


    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;
    private final StockHistoryRepository stockHistoryRepository;

    @Autowired
    public StockService(StockRepository stockRepository,
                        StoreRepository storeRepository,
                        StockHistoryRepository stockHistoryRepository) {
        this.stockRepository = stockRepository;
        this.storeRepository = storeRepository;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public void addStock(StockDto stockDto) {
        try {
            log.info("Starting stock addition - StoreId: {}, ProductId: {}, Quantity: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity());

            Store store = storeRepository.findById(Math.toIntExact(stockDto.getStoreId())).orElseThrow();

            Stock stock1 = stockRepository.findByStoreAndProductId(store, stockDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Stock not found for given store and product."));

            stock1.setQuantity(stock1.getQuantity() + stockDto.getQuantity());

            stockRepository.save(stock1);

            log.debug("Stock updated successfully");

            stockHistoryRepository.save(
                    StockHistory.builder()
                            .store(store)
                            .quantityChange(stockDto.getQuantity())
                            .reason(stockDto.getReason())
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }catch (Exception e){
            log.error("Failed to add stock - StoreId: {}, ProductId: {}, Quantity: {}. Error: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity(),
                    e.getMessage(), e);
            throw e;
        }
    }

    public void consumeStock(StockDto stockDto) {
        try {
            log.info("Starting stock consumption - StoreId: {}, ProductId: {}, Quantity: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity());
            Store store = storeRepository.findById(Math.toIntExact(stockDto.getStoreId())).orElseThrow();

            Stock stock = stockRepository.findByStoreAndProductId(store, stockDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Stock not found"));

            if (stock.getQuantity() < stockDto.getQuantity())
                throw new RuntimeException("Not enough stock");

            stock.setQuantity(stock.getQuantity()-stockDto.getQuantity());

            stockRepository.save(stock);
            log.debug("Stock consumed successfully - New quantity: {}", stock.getQuantity());

            stockHistoryRepository.save(
                    StockHistory.builder()
                            .store(store)
                            .quantityChange(stockDto.getQuantity())
                            .reason(stockDto.getReason())
                            .timestamp(LocalDateTime.now())
                            .build()
            );
            log.info("Successfully consumed stock - StoreId: {}, ProductId: {}, Quantity consumed: {}, Remaining: {}, Reason: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity(),
                    stock.getQuantity(), stockDto.getReason());
        }catch (Exception e){
            log.error("Failed to consume stock - StoreId: {}, ProductId: {}, Quantity: {}. Error: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity(),
                    e.getMessage(), e);
            throw e;
        }

    }
}
