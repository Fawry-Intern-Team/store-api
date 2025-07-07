package com.example.store_service.service;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.ReservedStock;
import com.example.store_service.model.Stock;
import com.example.store_service.model.StockHistory;
import com.example.store_service.model.Store;
import com.example.store_service.repositry.ReservedStockRepository;
import com.example.store_service.repositry.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservedStockService {

    private final StockRepository stockRepository;
    private final ReservedStockRepository reservedStockRepository;

    @Autowired
    public ReservedStockService(StockRepository stockRepository, ReservedStockRepository reservedStockRepository) {
        this.stockRepository = stockRepository;
        this.reservedStockRepository = reservedStockRepository;
    }

    public void reserveStock(Long orderId, List<ReservedStock> reservedStocks) {
        reservedStockRepository.saveAll(reservedStocks);
    }

    public void rollbackStock(Long orderId) {
        List<ReservedStock> reservedList = reservedStockRepository.findByOrderId(orderId);

        for (ReservedStock reserved : reservedList) {
            stockRepository.findByProductId(reserved.getProduct_id()).ifPresent(stock -> {
                stock.setQuantity(stock.getQuantity() + reserved.getQuantity());
                stockRepository.save(stock);
            });
        }
        reservedStockRepository.deleteByOrderId(orderId);
    }
}
