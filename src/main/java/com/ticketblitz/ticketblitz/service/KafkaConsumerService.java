package com.ticketblitz.ticketblitz.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final BookingService bookingService;

    public KafkaConsumerService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 🎧 THIS IS THE LISTENER
    // It runs in the background, independent of the user.
    @KafkaListener(topics = "ticket_bookings", groupId = "ticket-group")
    public void listen(String message) {
        System.out.println("📥 Kafka received: " + message);

        try {
            // 1. Parse the message "eventId:userId"
            String[] parts = message.split(":");
            Long eventId = Long.parseLong(parts[0]);
            Long userId = Long.parseLong(parts[1]);

            // 2. Process the Booking (uses Redis check + DB Lock)
            String result = bookingService.bookTicket(eventId, userId);

            System.out.println("✅ Processed: " + message + " -> " + result);

        } catch (Exception e) {
            System.err.println("❌ Error processing message: " + message);
        }
    }
}