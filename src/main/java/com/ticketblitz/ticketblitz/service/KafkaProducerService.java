package com.ticketblitz.ticketblitz.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "ticket_bookings";

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingRequest(Long eventId, Long userId) {
        // We create a simple string message like "1:500" (eventId:userId)
        // In real apps, we would use JSON, but this is faster for learning.
        String message = eventId + ":" + userId;

        System.out.println("📨 Sending to Kafka: " + message);
        kafkaTemplate.send(TOPIC, message);
    }
}