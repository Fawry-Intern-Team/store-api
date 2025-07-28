package com.example.store_service.service;

import com.example.store_service.model.Store;
import com.example.store_service.repositry.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class StoreService {


    private final StockService stockService;

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository,StockService stockService) {
        this.storeRepository = storeRepository;
        this.stockService = stockService;
        log.info("StoreService initialized");
    }

    public List<Store> getStores(){
        log.info("Fetching all stores");
        return storeRepository.findAll();
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

    public Store getStoreById(UUID id) {
        log.info("Fetching store by ID: {}", id);
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
    }
}
