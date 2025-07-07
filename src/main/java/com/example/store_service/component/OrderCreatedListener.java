package com.example.store_service.component;

import com.example.store_service.config.RabbitMQConfig;
import com.example.store_service.model.Stock;
import com.example.store_service.repositry.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.OrderCreatedEvent;
import org.example.events.OrderFailedEvent;
import org.example.events.OrderItemDTO;
import org.example.events.StockReservedEvent;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final RabbitTemplate rabbitTemplate;
    private final StockRepository stockRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        boolean allAvailable = true;


        if (allAvailable) {

            StockReservedEvent reserved = new StockReservedEvent();
            reserved.setOrderId(event.getOrderId());
            reserved.setItems(event.getItems());

            rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_RESERVED_QUEUE, reserved);
        } else {
            OrderFailedEvent failed = new OrderFailedEvent(
                    event.getOrderId(),
                    "Insufficient stock"
            );
            rabbitTemplate.convertAndSend("order.failed","", failed);
        }
    }

    @RabbitListener(queues = "store.order.failed")
    public void onOrderFailed(OrderFailedEvent event) {
        log.info("❌ Order " + event.getOrderId() + " marked as FAILED. Reason: " + event.getReason());

    }
}
