package com.example.store_service.component;

import com.example.store_service.config.RabbitMQConfig;
import com.example.store_service.model.ReservedStock;
import com.example.store_service.model.Stock;
import com.example.store_service.repositry.StockRepository;
import com.example.store_service.service.ReservedStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.OrderCreatedEvent;
import org.example.events.OrderFailedEvent;
import org.example.events.OrderItemDTO;
import org.example.events.StockReservedEvent;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final RabbitTemplate rabbitTemplate;
    private final ReservedStockService reservedStockService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            List<ReservedStock> stocks = new ArrayList<>();
            for (OrderItemDTO itemDTO : event.getItems()) {
                ReservedStock stock = ReservedStock.builder()
                        .orderId(event.getOrderId())
                        .quantity(itemDTO.getQuantity())
                        .productId(itemDTO.getProductId())
                        .build();
                stocks.add(stock);
            }
            reservedStockService.reserveStock(event.getOrderId(), stocks);
            rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_RESERVED_QUEUE, event);
        } catch (Exception e) {
            OrderFailedEvent failed = new OrderFailedEvent(
                    event.getOrderId(),
                    "Insufficient stock",
                    event.getTransactionId()
            );
            rabbitTemplate.convertAndSend("order.failed", "", failed);
        }
    }

    @RabbitListener(queues = "store.order.failed")
    public void onOrderFailed(OrderFailedEvent event) {
        try {

        reservedStockService.rollbackStock(event.getOrderId());
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
