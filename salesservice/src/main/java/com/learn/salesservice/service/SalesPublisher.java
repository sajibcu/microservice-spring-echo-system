package com.learn.salesservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.salesservice.events.ProductSoldEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SalesPublisher {

    private static final Logger log = LoggerFactory.getLogger(SalesPublisher.class);


    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange exchange;

    @Value("${spring.rabbitmq.routing-key}")
    private String ROUTING_KEY;

    public SalesPublisher(RabbitTemplate rabbitTemplate, TopicExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void publishProductSold(Long productId, int quantity) {
        try {
            ProductSoldEvent event = new ProductSoldEvent(productId, quantity);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(event); // Convert to JSON
            log.info("Publishing product sold event: {}", json);
            rabbitTemplate.convertAndSend(exchange.getName(), ROUTING_KEY, json);
        } catch (Exception e) {
            log.error("Failed to publish product sold event");
            e.printStackTrace();
        }
    }
}
