package com.microservices.billing_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BillingConsumer {

    @KafkaListener(topics = "order-topic", groupId = "billing-group")
    public void consume(String message) {
        System.out.println("Billing Service Received Order: " + message);
        System.out.println("Invoice Generated");
    }
}
