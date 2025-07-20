package com.example.store_service.service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.StockHistoryRepository;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.repositry.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
        log.info("StoreService initialized");
    }

    public Store createStore(Store store) {
        log.info("Starting store creation - ID: {}, Location: {}", store.getId(), store.getLocation());

        log.debug("Store details - Id: {}, Location: {}", store.getId(), store.getLocation());

        Optional<Store> existingStore = storeRepository.findByLocation(store.getLocation());
        if (existingStore.isPresent()) {
            log.warn("Store already exists at location: {}", store.getLocation());
            throw new IllegalArgumentException("Store already exists at location: " + store.getLocation());
        }

        Store savedStore = storeRepository.save(store);

        log.info("Successfully created store with ID: {}, Location: {}", savedStore.getId(), savedStore.getLocation());

        return savedStore;
    }
}
