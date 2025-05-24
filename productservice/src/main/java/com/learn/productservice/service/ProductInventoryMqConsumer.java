package com.learn.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.productservice.events.ProductSoldEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProductInventoryMqConsumer {

    private final ProductService productService;

    public ProductInventoryMqConsumer(ProductService productService) {
        this.productService = productService;
    }

    private static final Logger log = LoggerFactory.getLogger(ProductInventoryMqConsumer.class);

    // @RabbitListener(queues = "${spring.rabbitmq.queue-name}")
    @RabbitListener(queues = "product.inventory.queue" )
    public void consumeMessage(String message) {
        // Process the incoming message
        log.info("found the message in the queue");
        try {
            // Deserialize the JSON message into the Java class
            ObjectMapper objectMapper = new ObjectMapper();
            ProductSoldEvent event = objectMapper.readValue(message, ProductSoldEvent.class);

            // Access the fields
            Long productId = event.getProductId();
            int quantity = event.getQuantity();

            log.info("Received message: Product ID = {}, Quantity = {}", productId, quantity);
            productService.updateProductInventory(productId, quantity * (-1));
        } catch (Exception e) {
            log.error("Failed to deserialize message: {}", e.getMessage());
        }
    }
}
