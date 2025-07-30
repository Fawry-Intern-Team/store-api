package com.example.store_service.controller;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/store/stock")
public class StockController {
    private final StockService stockService;
    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }


    @GetMapping
    public ResponseEntity<?> getStocksByStoreId(
            @RequestParam UUID storeId,
            @RequestParam(defaultValue = "false") boolean paginated,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        if (!paginated) {
            // Return all stocks without pagination (existing behavior)
            return ResponseEntity.ok(stockService.getStocksByStoreId(storeId));
        }

        // Return paginated stocks
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Stock> stocksPage = stockService.getStocksByStoreIdPaginated(storeId, pageable);

        return ResponseEntity.ok(stocksPage);
    }


    @PostMapping("/add")
    public ResponseEntity<Void> addStock(@RequestBody @Valid StockDto stock, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        stockService.addStock(stock);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid StockDto stock, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        stockService.createStock(stock);
        return ResponseEntity.ok().build();
    }
    

    @PostMapping("/consume")
    public ResponseEntity<Void> consumeStock(@RequestBody @Valid StockDto stock, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        stockService.consumeStock(stock);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/products")
    public ResponseEntity<List<List<Stock>>> getProductsWithStore(@RequestBody List<UUID> productIds) {
        return ResponseEntity.ok(stockService.getProductsWithStore(productIds));
    }

}
