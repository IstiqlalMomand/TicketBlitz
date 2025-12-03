package com.ticketblitz.ticketblitz.controller;

import com.ticketblitz.ticketblitz.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ✅ POST Endpoint: To buy tickets
    @PostMapping
    public String bookTicket(@RequestParam Long eventId, @RequestParam Long userId) {
        return bookingService.bookTicket(eventId, userId);
    }

    // ✅ GET Endpoint: To check status in browser
    @GetMapping("/status")
    public String getEventStatus(@RequestParam Long eventId) {
        return bookingService.getEventStatus(eventId);
    }

    // ✅ GET Endpoint: The Audit
    @GetMapping("/audit")
    public String audit(@RequestParam Long eventId) {
        // 1. Get the Event details
        com.ticketblitz.ticketblitz.model.Event event = bookingService.getEvent(eventId);

        // 2. Count how many tickets we ACTUALLY sold in the database
        long actualSold = bookingService.countSoldTickets(eventId);

        // 3. Do the math
        long shouldBeAvailable = event.getTotalTickets() - actualSold;
        long databaseSaysAvailable = event.getAvailableTickets();

        if (databaseSaysAvailable > shouldBeAvailable) {
            long phantomTickets = databaseSaysAvailable - shouldBeAvailable;
            return "❌ BUG DETECTED! We oversold! <br>" +
                    "Total Seats: " + event.getTotalTickets() + "<br>" +
                    "Actual Sold: " + actualSold + "<br>" +
                    "Database Says: " + databaseSaysAvailable + "<br>" +
                    "⚠️ PHANTOM TICKETS (Lost Updates): " + phantomTickets;
        } else {
            return "✅ Data looks clean... (Available: " + databaseSaysAvailable + ")";
        }
    }
}