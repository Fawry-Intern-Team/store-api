package com.example.store_service.controller;

import com.example.store_service.model.StockHistory;
import com.example.store_service.service.StockHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/store/{storeId}/history")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    @Autowired
    public StockHistoryController(StockHistoryService stockHistoryService) {
        this.stockHistoryService = stockHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<StockHistory>> getStockHistoryForStore(@PathVariable UUID storeId){
        return ResponseEntity.ok(stockHistoryService.getStockHistoryForStore(storeId));
    }
}
