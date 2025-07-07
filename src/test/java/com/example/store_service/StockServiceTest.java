// StockServiceTest.java
package com.example.store_service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.repositry.StoreRepository;
import com.example.store_service.service.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @InjectMocks
    private StockService stockService;

    private Store testStore;
    private Stock testStock;
    private StockDto testStockDto;

    @BeforeEach
    void setUp() {
        testStore = new Store();
        testStore.setId(1L);
        testStore.setLocation("Test Location");

        testStock = Stock.builder()
                .id(1L)
                .store(testStore)
                .product_id(100L)
                .quantity(50)
                .build();

        testStockDto = new StockDto();
        testStockDto.setStoreId(1L);
        testStockDto.setProductId(100L);
        testStockDto.setQuantity(10);
        testStockDto.setReason("Test Reason");
    }

    @Test
    void addStock_Success() {
        // Arrange
        when(storeRepository.findById(anyInt())).thenReturn(Optional.of(testStore));
        when(stockRepository.findByStoreAndProductId(testStore, 100L)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);
        when(stockHistoryRepository.save(any(StockHistory.class))).thenReturn(new StockHistory());

        // Act
        stockService.addStock(testStockDto);

        // Assert
        verify(storeRepository).findById(1);
        verify(stockRepository).findByStoreAndProductId(testStore, 100L);
        verify(stockRepository).save(testStock);
        verify(stockHistoryRepository).save(any(StockHistory.class));
        assertEquals(60, testStock.getQuantity()); // 50 + 10
    }

    @Test
    void addStock_StoreNotFound() {
        // Arrange
        when(storeRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> stockService.addStock(testStockDto));
        verify(stockRepository, never()).save(any(Stock.class));
        verify(stockHistoryRepository, never()).save(any(StockHistory.class));
    }

    @Test
    void addStock_StockNotFound() {
        // Arrange
        when(storeRepository.findById(anyInt())).thenReturn(Optional.of(testStore));
        when(stockRepository.findByStoreAndProductId(testStore, 100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> stockService.addStock(testStockDto));
        verify(stockRepository, never()).save(any(Stock.class));
        verify(stockHistoryRepository, never()).save(any(StockHistory.class));
    }


    @Test
    void consumeStock_Success() {
        // Arrange
        when(storeRepository.findById(anyInt())).thenReturn(Optional.of(testStore));
        when(stockRepository.findByStoreAndProductId(testStore, 100L)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);
        when(stockHistoryRepository.save(any(StockHistory.class))).thenReturn(new StockHistory());

        // Act
        stockService.consumeStock(testStockDto);

        // Assert
        verify(storeRepository).findById(1);
        verify(stockRepository).findByStoreAndProductId(testStore, 100L);
        verify(stockRepository).save(testStock);
        verify(stockHistoryRepository).save(any(StockHistory.class));
        assertEquals(40, testStock.getQuantity()); // 50 - 10
    }

    @Test
    void consumeStock_NotEnoughStock() {
        // Arrange
        testStockDto.setQuantity(60); // More than available (50)
        when(storeRepository.findById(anyInt())).thenReturn(Optional.of(testStore));
        when(stockRepository.findByStoreAndProductId(testStore, 100L)).thenReturn(Optional.of(testStock));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> stockService.consumeStock(testStockDto));
        assertEquals("Not enough stock", exception.getMessage());
        verify(stockRepository, never()).save(any(Stock.class));
        verify(stockHistoryRepository, never()).save(any(StockHistory.class));
    }

    @Test
    void consumeStock_StockNotFound() {
        // Arrange
        when(storeRepository.findById(anyInt())).thenReturn(Optional.of(testStore));
        when(stockRepository.findByStoreAndProductId(testStore, 100L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> stockService.consumeStock(testStockDto));
        assertEquals("Stock not found", exception.getMessage());
        verify(stockRepository, never()).save(any(Stock.class));
        verify(stockHistoryRepository, never()).save(any(StockHistory.class));
    }
}