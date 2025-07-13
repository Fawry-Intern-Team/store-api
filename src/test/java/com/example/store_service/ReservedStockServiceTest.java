package com.example.store_service;

import com.example.store_service.model.ReservedStock;
import com.example.store_service.model.Stock;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.ReservedStockRepository;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.service.ReservedStockService;
import org.example.events.OrderCreatedEvent;
import org.example.events.OrderItemDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservedStockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ReservedStockRepository reservedStockRepository;

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @InjectMocks
    private ReservedStockService reservedStockService;

    private final Long orderId = 1L;

    @Test
    void reserveStock_shouldReserveSuccessfully() {
        // Arrange
        Long storeId = 10L;
        Long productId = 20L;
        int quantity = 5;

        Stock stock = Stock.builder()
                .storeId(storeId)
                .productId(productId)
                .quantity(10)
                .build();

        OrderItemDTO item = OrderItemDTO.builder()
                .storeId(storeId)
                .productId(productId)
                .quantity(quantity)
                .build();

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(orderId)
                .items(List.of(item))
                .build();

        when(stockRepository.findByStoreIdAndProductId(storeId, productId)).thenReturn(Optional.of(stock));

        // Act
        reservedStockService.reserveStock(event);

        // Assert
        verify(reservedStockRepository).save(any(ReservedStock.class));
        verify(stockRepository).save(argThat(s -> s.getQuantity() == 5));
        verify(stockHistoryRepository).save(any());
    }

    @Test
    void reserveStock_shouldThrowIfStockNotFound() {
        // Arrange
        Long storeId = 1L;
        Long productId = 2L;

        OrderItemDTO item = OrderItemDTO.builder()
                .storeId(storeId)
                .productId(productId)
                .quantity(1)
                .build();

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(100L)
                .items(List.of(item))
                .build();

        when(stockRepository.findByStoreIdAndProductId(storeId, productId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> reservedStockService.reserveStock(event));
        assertTrue(thrown.getMessage().contains("Stock not found for productId"));
    }

    @Test
    void reserveStock_shouldThrowIfInsufficientStock() {
        // Arrange
        Long storeId = 1L;
        Long productId = 2L;

        Stock stock = Stock.builder()
                .storeId(storeId)
                .productId(productId)
                .quantity(3)
                .build();

        OrderItemDTO item = OrderItemDTO.builder()
                .storeId(storeId)
                .productId(productId)
                .quantity(5)
                .build();

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(200L)
                .items(List.of(item))
                .build();

        when(stockRepository.findByStoreIdAndProductId(storeId, productId)).thenReturn(Optional.of(stock));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> reservedStockService.reserveStock(event));
        assertTrue(thrown.getMessage().contains("Insufficient stock"));
    }

    @Test
    void rollbackStock_shouldRestoreStockAndDeleteReserved() {
        // Arrange
        Long orderId = 1L;
        Long productId = 100L;

        ReservedStock reservedStock = ReservedStock.builder()
                .orderId(orderId)
                .productId(productId)
                .quantity(3)
                .build();

        Stock stock = Stock.builder()
                .productId(productId)
                .quantity(5)
                .build();

        when(reservedStockRepository.findByOrderId(orderId)).thenReturn(List.of(reservedStock));
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(stock));

        // Act
        reservedStockService.rollbackStock(orderId);

        // Assert
        verify(stockRepository).save(argThat(updatedStock ->
                updatedStock.getProductId().equals(productId) &&
                        updatedStock.getQuantity() == 8
        ));

        // 2. ReservedStock deleted
        verify(reservedStockRepository).deleteAll(List.of(reservedStock));
        verify(stockHistoryRepository).save(any());
    }

    @Test
    void rollbackStock_shouldSkipIfNoReservedStock() {
        // Arrange
        Long orderId = 2L;

        when(reservedStockRepository.findByOrderId(orderId)).thenReturn(Collections.emptyList());

        // Act
        reservedStockService.rollbackStock(orderId);

        // Assert
        verify(stockRepository, never()).save(any());
        verify(reservedStockRepository, never()).deleteAll(any());
    }

    @Test
    void rollbackStock_shouldHandleMissingStock() {
        // Arrange
        Long orderId = 3L;
        Long productId = 200L;

        ReservedStock reservedStock = ReservedStock.builder()
                .orderId(orderId)
                .productId(productId)
                .quantity(5)
                .build();

        when(reservedStockRepository.findByOrderId(orderId)).thenReturn(List.of(reservedStock));
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // Act
        reservedStockService.rollbackStock(orderId);

        // Assert
        verify(stockRepository, never()).save(any());
        verify(reservedStockRepository).deleteAll(List.of(reservedStock));
    }
}
