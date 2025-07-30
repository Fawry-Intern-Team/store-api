package com.example.store_service.controller;

import com.example.store_service.model.Store;
import com.example.store_service.service.StoreService;
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
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable UUID id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }


    @PostMapping("/create")
    public ResponseEntity<Store> createStore(@RequestBody @Valid Store store, @RequestHeader(value = "X-API-Version", defaultValue = "v1") String version){
        return ResponseEntity.ok(storeService.createStore(store));
    }

    @GetMapping
    public ResponseEntity<?> getStoresPaginated(
            @RequestParam(defaultValue = "false") boolean paginated,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "location") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        if (!paginated) {
            // Return all stores without pagination (existing behavior)
            return ResponseEntity.ok(storeService.getStores());
        }

        // Return paginated stores
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Store> storesPage = storeService.getStoresPaginated(pageable);

        return ResponseEntity.ok(storesPage);
    }

}
