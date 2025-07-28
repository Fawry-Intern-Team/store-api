package com.example.store_service.controller;

import com.example.store_service.dto.StockDto;
import com.example.store_service.dto.StoreProductResponse;
import com.example.store_service.model.Stock;
import com.example.store_service.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/store")
public class StockController {
    private final StockService stockService;
    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{storeId}/stock")
    public ResponseEntity<List<Stock>> getStocksByStoreId(@PathVariable UUID storeId) {
        return ResponseEntity.ok(stockService.getStocksByStoreId(storeId));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/stock/add")
    public ResponseEntity<Void> addStock(@RequestBody @Valid StockDto stock, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        stockService.addStock(stock);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/stock/create")
    public ResponseEntity<Void> create(@RequestBody @Valid StockDto stock, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        stockService.createStock(stock);
        return ResponseEntity.ok().build();
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/stock/consume")
    public ResponseEntity<Void> consumeStock(@RequestBody @Valid StockDto stock, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        stockService.consumeStock(stock);
        return ResponseEntity.ok().build();
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/stock/products")
    public ResponseEntity<List<List<Stock>>> getProductsWithStore(@RequestBody List<UUID> productIds) {
        return ResponseEntity.ok(stockService.getProductsWithStore(productIds));
    }
}
