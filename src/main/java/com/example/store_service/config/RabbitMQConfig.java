package com.example.store_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String STOCK_RESERVED_QUEUE = "stock.reserved.queue";
    public static final String ORDER_FAILED_QUEUE = "store.order.failed";
    @Bean
    public FanoutExchange orderFailedExchange() {
        return new FanoutExchange("order.failed");
    }
    @Bean
    public Queue orderFailedQueue() {
        return new Queue(ORDER_FAILED_QUEUE);
    }

    @Bean
    public Binding exchangeBinding(FanoutExchange orderFailedExchange) {
        return BindingBuilder.bind(orderFailedQueue()).to(orderFailedExchange);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE);
    }

    @Bean
    public Queue stockReservedQueue() {
        return new Queue(STOCK_RESERVED_QUEUE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
