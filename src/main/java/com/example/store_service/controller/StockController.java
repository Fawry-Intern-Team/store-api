package com.example.store_service.controller;

import com.example.store_service.dto.StockDto;
import com.example.store_service.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store/stock")
public class StockController {
    private final StockService stockService;
    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addStock(@RequestBody @Valid @RequestHeader(value = "X-API-Version", defaultValue = "v1")StockDto stock){
        stockService.addStock(stock);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid @RequestHeader(value = "X-API-Version", defaultValue = "v1")StockDto stock){
        stockService.createStock(stock);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/consume")
    public ResponseEntity<Void> consumeStock(@RequestBody @Valid @RequestHeader(value = "X-API-Version", defaultValue = "v1")StockDto stock){
        stockService.consumeStock(stock);
        return ResponseEntity.ok().build();
    }
}
