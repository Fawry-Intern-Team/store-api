package com.example.store_service.controller;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.ReservedStock;
import com.example.store_service.service.ReservedStockService;
import jakarta.validation.Valid;
import org.example.events.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/stock")
public class ReservedStockController {
    private final ReservedStockService reservedStockService;

    @Autowired
    public ReservedStockController(ReservedStockService reservedStockService) {
        this.reservedStockService = reservedStockService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveStock(@RequestBody @Valid  OrderCreatedEvent orderCreatedEvent, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        reservedStockService.reserveStock(orderCreatedEvent);
        return ResponseEntity.ok().build();
    }
}
