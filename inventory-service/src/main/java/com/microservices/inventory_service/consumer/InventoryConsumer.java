package com.microservices.inventory_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(String message) {
        System.out.println("Inventory Service Received Order: " + message);
        System.out.println("Stock Updated");
    }
}

