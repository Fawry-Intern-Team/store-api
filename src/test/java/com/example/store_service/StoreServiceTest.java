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

import java.util.Optional;
import java.util.UUID;

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
        testStore.setId(UUID.nameUUIDFromBytes("1".getBytes()));
        testStore.setLocation("Test Location");
    }

    @Test
    void createStore_Success() {
        // Arrange
        when(storeRepository.findByLocation("Test Location")).thenReturn(Optional.empty());
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        // Act
        Store result = storeService.createStore(testStore);

        // Assert
        assertNotNull(result);
        assertEquals("Test Location", result.getLocation());
        assertEquals(UUID.nameUUIDFromBytes("1".getBytes()), result.getId());

        verify(storeRepository).findByLocation("Test Location");
        verify(storeRepository).save(testStore);
    }

    @Test
    void createStore_StoreAlreadyExists_ThrowsException() {
        // Arrange
        when(storeRepository.findByLocation("Test Location")).thenReturn(Optional.of(testStore));

        // Act & Assert
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> storeService.createStore(testStore)
        );

        assertEquals("Store already exists at location: Test Location", exception.getMessage());
        verify(storeRepository).findByLocation("Test Location");
    }
}
