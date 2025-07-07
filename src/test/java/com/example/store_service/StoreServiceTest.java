package com.example.store_service;

import com.example.store_service.model.Store;
import com.example.store_service.repositry.StoreRepository;
import com.example.store_service.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private Store testStore;

    @BeforeEach
    void setUp() {
        testStore = new Store();
        testStore.setId(1L);
        testStore.setLocation("Test Location");
    }

    @Test
    void createStore_Success() {
        // Arrange
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        // Act
        Store result = storeService.createStore(testStore);

        // Assert
        assertNotNull(result);
        assertEquals("Test Location", result.getLocation());
        assertEquals(1L, result.getId());
        verify(storeRepository).save(testStore);
    }
}
