package com.example.store_service.service;

import com.example.store_service.dto.StockDto;

import com.example.store_service.model.ReservedStock;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.ReservedStockRepository;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.events.OrderCreatedEvent;
import org.example.events.OrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReservedStockService {

    private final StockRepository stockRepository;
    private final ReservedStockRepository reservedStockRepository;
    private final StockHistoryRepository stockHistoryRepository;

    @Autowired
    public ReservedStockService(StockRepository stockRepository, ReservedStockRepository reservedStockRepository, StockHistoryRepository stockHistoryRepository) {
        this.stockRepository = stockRepository;
        this.reservedStockRepository = reservedStockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
        log.info("ReservedStockService initialized");
    }


    public void reserveStock(OrderCreatedEvent orderCreatedEvent) {
        log.info("Starting stock reservation for orderId: {}", orderCreatedEvent.getOrderId());

        for (OrderItemDTO reserved : orderCreatedEvent.getItems()) {
            Stock stock = stockRepository.findByStoreIdAndProductId(
                    reserved.getStoreId(), reserved.getProductId()
            ).orElseThrow(() ->
                    new EntityNotFoundException("Stock not found for productId " + reserved.getProductId()));

            if (stock.getQuantity() < reserved.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for productId " + reserved.getProductId());
            }

            ReservedStock reservedStock = ReservedStock.builder()
                    .orderId(orderCreatedEvent.getOrderId())
                    .storeId(reserved.getStoreId())
                    .productId(reserved.getProductId())
                    .quantity(reserved.getQuantity())
                    .build();

            reservedStockRepository.save(reservedStock);

            stock.setQuantity(stock.getQuantity() - reserved.getQuantity());
            stockRepository.save(stock);

            stockHistoryRepository.save(
                    StockHistory.builder()
                            .storeId(reserved.getStoreId())
                            .productId(reserved.getProductId())
                            .quantityChange(-reserved.getQuantity())
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }

        log.info("Successfully reserved and consumed stock for orderId: {} with {} items",
                orderCreatedEvent.getOrderId(), orderCreatedEvent.getItems().size());
    }


    public void rollbackStock(UUID orderId) {
        log.info("Starting stock rollback for orderId: {}", orderId);

        List<ReservedStock> reservedList = reservedStockRepository.findByOrderId(orderId);

        if (reservedList.isEmpty()) {
            log.warn("No reserved stock found for orderId: {} during rollback", orderId);
            return;
        }

        log.debug("Found {} reserved items to rollback for orderId: {}", reservedList.size(), orderId);

        for (ReservedStock reserved : reservedList) {
            log.debug("Rolling back stock - ProductId: {}, Quantity: {}",
                    reserved.getProductId(), reserved.getQuantity());

            Stock stock = stockRepository.findByStoreIdAndProductId(reserved.getStoreId(),reserved.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Stock not found for productId " + reserved.getProductId()));

            int oldQuantity = stock.getQuantity();
            int newQuantity = oldQuantity + reserved.getQuantity();

            stock.setQuantity(newQuantity);
            stockRepository.save(stock);

            stockHistoryRepository.save(
                    StockHistory.builder()
                            .storeId(reserved.getStoreId())
                            .productId(reserved.getProductId())
                            .quantityChange(reserved.getQuantity())
                            .timestamp(LocalDateTime.now())
                            .build()
            );

            log.debug("Stock updated for ProductId: {} - Old quantity: {}, New quantity: {}",
                    reserved.getProductId(), oldQuantity, newQuantity);
        }

        reservedStockRepository.deleteAll(reservedList);

        log.info("Successfully rolled back stock for orderId: {} with {} items",
                orderId, reservedList.size());
    }

}
