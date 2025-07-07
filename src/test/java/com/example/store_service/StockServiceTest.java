package com.example.store_service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.repositry.StoreRepository;
import com.example.store_service.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddStock_success() {
        StockDto dto = new StockDto(1, 2, 10, "Restock");
        Store store = new Store();
        Stock stock = new Stock();
        stock.setQuantity(5);

        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(stockRepository.findByStoreAndProductId(store, 2)).thenReturn(Optional.of(stock));

        stockService.addStock(dto);

        assertEquals(15, stock.getQuantity());
        verify(stockRepository).save(stock);
        verify(stockHistoryRepository).save(any(StockHistory.class));
    }

    @Test
    public void testConsumeStock_success() {
        StockDto dto = new StockDto(1, 2, 5, "Order");
        Store store = new Store();
        Stock stock = new Stock();
        stock.setQuantity(10);

        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(stockRepository.findByStoreAndProductId(store, 2)).thenReturn(Optional.of(stock));

        stockService.consumeStock(dto);

        assertEquals(5, stock.getQuantity());
        verify(stockRepository).save(stock);
        verify(stockHistoryRepository).save(any(StockHistory.class));
    }

    @Test
    public void testConsumeStock_insufficientQuantity_throwsException() {
        StockDto dto = new StockDto(1, 2, 15, "Order");
        Store store = new Store();
        store.setId(1);
        Stock stock = new Stock();
        stock.setQuantity(5);

        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        when(stockRepository.findByStoreAndProductId(store, 2)).thenReturn(Optional.of(stock));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            stockService.consumeStock(dto);
        });

        assertEquals("Not enough stock", ex.getMessage());
    }
}
