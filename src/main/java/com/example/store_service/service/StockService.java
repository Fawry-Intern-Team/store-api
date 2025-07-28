package com.example.store_service.service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.mapper.StockMapper;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.repositry.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class StockService {
    @Autowired
    private StockMapper stockMapper;

    private final RestTemplate restTemplate;

    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;
    private final StockHistoryRepository stockHistoryRepository;

    @Autowired
    public StockService(StockRepository stockRepository,
                        StoreRepository storeRepository,
                        StockHistoryRepository stockHistoryRepository,
                        RestTemplate restTemplate) {
        this.stockRepository = stockRepository;
        this.storeRepository = storeRepository;
        this.stockHistoryRepository = stockHistoryRepository;
        this.restTemplate = restTemplate;
    }
    public void validateProductExists(UUID productId) {
        String productServiceUrl = "http://localhost:8081/product?id=" + productId;
        ResponseEntity<String> response = restTemplate.getForEntity(productServiceUrl, String.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
    }

    private void saveStockHistory(UUID storeId, UUID productId, int quantityChange, String reason) {
        stockHistoryRepository.save(
                StockHistory.builder()
                        .storeId(storeId)
                        .productId(productId)
                        .quantityChange(quantityChange)
                        .reason(reason)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    public void createStock(StockDto stockDto) {

            log.info("Starting stock creation - StoreId: {}, ProductId: {}, Quantity: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity());

            UUID productId = stockDto.getProductId();
            validateProductExists(productId);

            Store store = storeRepository.findById(stockDto.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store not found with ID: " + stockDto.getStoreId()));

            Optional<Stock> existingStock = stockRepository.findByStoreIdAndProductId(stockDto.getStoreId(), productId);
            if (existingStock.isPresent()) {
                throw new RuntimeException("Stock is already created for this store and product.");
            }
            saveStockHistory(store.getId(), productId, stockDto.getQuantity(), stockDto.getReason());

            Stock stock = stockMapper.toEntity(stockDto);
            stockRepository.save(stock);

            log.debug("Stock created successfully");

    }

    public void addStock(StockDto stockDto) {

            log.info("Starting stock addition - StoreId: {}, ProductId: {}, Quantity: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity());

            UUID productId = stockDto.getProductId();
            validateProductExists(productId);

            Store store = storeRepository.findById(stockDto.getStoreId())
                    .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + stockDto.getStoreId()));

            Stock stock = stockRepository.findByStoreIdAndProductId(stockDto.getStoreId(), productId)
                    .orElseThrow(() -> new EntityNotFoundException("Stock not found for given store and product."));

            stock.setQuantity(stock.getQuantity() + stockDto.getQuantity());
            stockRepository.save(stock);

            saveStockHistory(store.getId(), productId, stockDto.getQuantity(), stockDto.getReason());

            log.debug("Stock updated successfully");
    }

    public void consumeStock(StockDto stockDto) {

            log.info("Starting stock consumption - StoreId: {}, ProductId: {}, Quantity: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity());

            UUID productId = stockDto.getProductId();
            validateProductExists(productId);

            Store store = storeRepository.findById(stockDto.getStoreId())
                    .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + stockDto.getStoreId()));

            Stock stock = stockRepository.findByStoreIdAndProductId(stockDto.getStoreId(), stockDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Stock not found for given store and product."));

            if (stock.getQuantity() < stockDto.getQuantity()) {
                throw new IllegalStateException("Not enough stock");
            }

            stock.setQuantity(stock.getQuantity() - stockDto.getQuantity());
            stockRepository.save(stock);

            log.debug("Stock consumed successfully - New quantity: {}", stock.getQuantity());

            saveStockHistory(store.getId(), productId, -stockDto.getQuantity(), stockDto.getReason());

            log.info("Successfully consumed stock - StoreId: {}, ProductId: {}, Quantity consumed: {}, Remaining: {}, Reason: {}",
                    stockDto.getStoreId(), stockDto.getProductId(), stockDto.getQuantity(),
                    stock.getQuantity(), stockDto.getReason());
    }

    public List<Stock> getStocksByStoreId(UUID storeId) {
        log.info("Fetching stock ");
        return stockRepository.findByStoreId(storeId).orElseThrow();
    }

    public List<List<Stock>> getProductsWithStore(List<UUID> productIds) {
        List<List<Stock>> validStocks = new ArrayList<>();

        for (UUID productId : productIds) {
            validateProductExists(productId); // check via REST
            List<Stock> stocks = stockRepository.findStocksByProductId(productId);
            validStocks.add(stocks); // add list (even if empty)
        }

        System.out.println(validStocks);
        return validStocks;
    }
}
