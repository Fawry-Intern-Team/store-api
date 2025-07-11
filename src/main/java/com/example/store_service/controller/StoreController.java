package com.example.store_service.controller;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.model.Store;
import com.example.store_service.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/create")
    public ResponseEntity<Store> createStore(@RequestBody @Valid @RequestHeader(value = "X-API-Version", defaultValue = "v1") Store store){
        return ResponseEntity.ok(storeService.createStore(store));
    }
}
