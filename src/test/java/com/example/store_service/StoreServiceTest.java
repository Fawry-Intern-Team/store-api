package com.example.store_service;

import com.example.store_service.model.Store;
import com.example.store_service.repositry.StoreRepository;
import com.example.store_service.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStore() {
        Store store = new Store();
        store.setId(1);
        store.setLocation("cairo");

        when(storeRepository.save(store)).thenReturn(store);

        Store result = storeService.createStore(store);

        assertEquals("cairo", result.getLocation());
        verify(storeRepository).save(store);
    }
}
