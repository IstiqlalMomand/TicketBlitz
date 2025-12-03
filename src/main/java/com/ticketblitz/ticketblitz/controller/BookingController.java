package com.ticketblitz.ticketblitz.controller;

import com.ticketblitz.ticketblitz.service.BookingService;
import com.ticketblitz.ticketblitz.service.KafkaProducerService; // ✅ Import the Producer
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final KafkaProducerService kafkaProducerService; // ✅ Inject Kafka

    // Update Constructor to include KafkaProducerService
    public BookingController(BookingService bookingService, KafkaProducerService kafkaProducerService) {
        this.bookingService = bookingService;
        this.kafkaProducerService = kafkaProducerService;
    }

    // ✅ POST Endpoint: Now Async!
    @PostMapping
    public String bookTicket(@RequestParam Long eventId, @RequestParam Long userId) {
        // OLD WAY: return bookingService.bookTicket(eventId, userId);

        // NEW WAY: Send message to Kafka and return immediately
        kafkaProducerService.sendBookingRequest(eventId, userId);

        return "⏳ Request Received! We are processing it in the background.";
    }

    @GetMapping("/status")
    public String getEventStatus(@RequestParam Long eventId) {
        return bookingService.getEventStatus(eventId);
    }

    @GetMapping("/audit")
    public String audit(@RequestParam Long eventId) {
        com.ticketblitz.ticketblitz.model.Event event = bookingService.getEvent(eventId);
        long actualSold = bookingService.countSoldTickets(eventId);
        long shouldBeAvailable = event.getTotalTickets() - actualSold;
        long databaseSaysAvailable = event.getAvailableTickets();

        if (databaseSaysAvailable > shouldBeAvailable) {
            long phantomTickets = databaseSaysAvailable - shouldBeAvailable;
            return "❌ BUG DETECTED! We oversold! <br>" +
                    "Total Seats: " + event.getTotalTickets() + "<br>" +
                    "Actual Sold: " + actualSold + "<br>" +
                    "Database Says: " + databaseSaysAvailable + "<br>" +
                    "⚠️ PHANTOM TICKETS: " + phantomTickets;
        } else {
            return "✅ Data looks clean... (Available: " + databaseSaysAvailable + ")";
        }
    }
}