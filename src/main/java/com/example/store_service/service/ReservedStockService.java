package com.example.store_service.service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.ReservedStock;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.ReservedStockRepository;
import com.example.store_service.repositry.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReservedStockService {

    private final StockRepository stockRepository;
    private final ReservedStockRepository reservedStockRepository;

    @Autowired
    public ReservedStockService(StockRepository stockRepository, ReservedStockRepository reservedStockRepository) {
        this.stockRepository = stockRepository;
        this.reservedStockRepository = reservedStockRepository;
        log.info("ReservedStockService initialized");
    }

    public void reserveStock(Long orderId, List<ReservedStock> reservedStocks) {
        log.info("Starting stock reservation for orderId: {}", orderId);
        try {
            reservedStockRepository.saveAll(reservedStocks);
            log.info("Successfully reserved stock for orderId: {} with {} items",
                    orderId, reservedStocks.size());
        } catch (Exception e) {
            log.error("Failed to reserve stock for orderId: {}. Error: {}",
                    orderId, e.getMessage(), e);
            throw e;
        }

    }

    public void rollbackStock(Long orderId) {
        log.info("Starting stock rollback for orderId: {}", orderId);
        try {
            List<ReservedStock> reservedList = reservedStockRepository.findByOrderId(orderId);

            if (reservedList.isEmpty()) {
                log.warn("No reserved stock found for orderId: {} during rollback", orderId);
                return;
            }

            log.debug("Found {} reserved items to rollback for orderId: {}",
                    reservedList.size(), orderId);

            for (ReservedStock reserved : reservedList) {
                log.debug("Rolling back stock - ProductId: {}, Quantity: {}",
                        reserved.getProductId(), reserved.getQuantity());

                stockRepository.findByProductId(reserved.getProductId()).ifPresent(stock -> {
                    int oldQuantity = stock.getQuantity();
                    int newQuantity = oldQuantity + reserved.getQuantity();

                    stock.setQuantity(newQuantity);
                    stockRepository.save(stock);

                    log.debug("Stock updated for ProductId: {} - Old quantity: {}, New quantity: {}",
                            reserved.getProductId(), oldQuantity, newQuantity);
                });

            }

            reservedStockRepository.deleteAll(reservedList);

            log.info("Successfully rolled back stock for orderId: {} with {} items",
                    orderId, reservedList.size());

        } catch (Exception e) {
            log.error("Failed to rollback stock for orderId: {}. Error: {}",
                    orderId, e.getMessage(), e);
            throw e;
        }
    }
}
