package com.example.store_service.controller;

import com.example.store_service.model.StockHistory;
import com.example.store_service.service.StockHistoryService;
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
@RequestMapping("/api/store")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    @Autowired
    public StockHistoryController(StockHistoryService stockHistoryService) {
        this.stockHistoryService = stockHistoryService;
    }

    @GetMapping("/{stockId}/history")
    public ResponseEntity<?> getStockHistoryForStore(
            @PathVariable UUID stockId,
            @RequestParam(defaultValue = "false") boolean paginated,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        if (!paginated) {
            // Return all history without pagination (existing behavior)
            return ResponseEntity.ok(stockHistoryService.getStockHistoryForStore(stockId));
        }

        // Return paginated history
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<StockHistory> historyPage = stockHistoryService.getStockHistoryForStorePaginated(stockId, pageable);

        return ResponseEntity.ok(historyPage);
    }
}
