package com.example.store_service;


import com.example.store_service.model.ReservedStock;
import com.example.store_service.model.Stock;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.ReservedStockRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.service.ReservedStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservedStockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ReservedStockRepository reservedStockRepository;

    @InjectMocks
    private ReservedStockService reservedStockService;

    private List<ReservedStock> testReservedStocks;
    private Stock testStock;
    private Long testOrderId;

    @BeforeEach
    void setUp() {
        testOrderId = 1L;

        ReservedStock reservedStock1 = new ReservedStock();
        reservedStock1.setId(1L);
        reservedStock1.setOrder_id(testOrderId);
        reservedStock1.setProduct_id(100L);
        reservedStock1.setQuantity(5);

        ReservedStock reservedStock2 = new ReservedStock();
        reservedStock2.setId(2L);
        reservedStock2.setOrder_id(testOrderId);
        reservedStock2.setProduct_id(200L);
        reservedStock2.setQuantity(3);

        testReservedStocks = Arrays.asList(reservedStock1, reservedStock2);

        testStock = Stock.builder()
                .id(1L)
                .store(new Store())
                .product_id(100L)
                .quantity(20)
                .build();
    }

    @Test
    void reserveStock_Success() {
        // Arrange
        when(reservedStockRepository.saveAll(testReservedStocks)).thenReturn(testReservedStocks);

        // Act
        reservedStockService.reserveStock(testOrderId, testReservedStocks);

        // Assert
        verify(reservedStockRepository).saveAll(testReservedStocks);
    }

    @Test
    void rollbackStock_Success() {
        // Arrange
        when(reservedStockRepository.findByOrderId(testOrderId)).thenReturn(testReservedStocks);
        when(stockRepository.findByProductId(100L)).thenReturn(Optional.of(testStock));
        when(stockRepository.findByProductId(200L)).thenReturn(Optional.empty());
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        // Act
        reservedStockService.rollbackStock(testOrderId);

        // Assert
        verify(reservedStockRepository).findByOrderId(testOrderId);
        verify(stockRepository).findByProductId(100L);
        verify(stockRepository).findByProductId(200L);
        verify(stockRepository).save(testStock);
        verify(reservedStockRepository).deleteByOrderId(testOrderId);
        assertEquals(25, testStock.getQuantity()); // 20 + 5
    }

    @Test
    void rollbackStock_EmptyReservedList() {
        // Arrange
        when(reservedStockRepository.findByOrderId(testOrderId)).thenReturn(Arrays.asList());

        // Act
        reservedStockService.rollbackStock(testOrderId);

        // Assert
        verify(reservedStockRepository).findByOrderId(testOrderId);
        verify(stockRepository, never()).findByProductId(anyLong());
        verify(stockRepository, never()).save(any(Stock.class));
        verify(reservedStockRepository).deleteByOrderId(testOrderId);
    }
}
