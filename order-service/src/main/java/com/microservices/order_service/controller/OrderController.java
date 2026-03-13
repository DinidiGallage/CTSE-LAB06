package com.microservices.order_service.controller;

import com.microservices.order_service.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "order-topic";

    @PostMapping
    public String createOrder(@RequestBody Order order) {
        String message = String.format("Order[id=%s, product=%s, quantity=%d, price=%.2f]",
                order.getOrderId(), order.getProductName(), order.getQuantity(), order.getPrice());
        
        kafkaTemplate.send(TOPIC, message);
        
        return "Order Created & Event Published";
    }
}

