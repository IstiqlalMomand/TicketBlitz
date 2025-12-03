package com.ticketblitz.ticketblitz.service;

import com.ticketblitz.ticketblitz.model.Event;
import com.ticketblitz.ticketblitz.model.Ticket;
import com.ticketblitz.ticketblitz.repository.EventRepository;
import com.ticketblitz.ticketblitz.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final RedisService redisService; // ✅ Inject Redis

    public BookingService(EventRepository eventRepository, TicketRepository ticketRepository, RedisService redisService) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.redisService = redisService;
    }

    @Transactional
    public String bookTicket(Long eventId, Long userId) {
        // 🚀 STEP 1: Fast Check (Redis)
        // We decrement in memory. This happens in microseconds.
        Long newCount = redisService.decrementTicketCount(eventId);

        if (newCount < 0) {
            // If Redis says we are out, we reject immediately.
            // We don't even touch the Database.
            return "❌ Redis says: Sold out!";
        }

        // 🐢 STEP 2: Slow Check (Database)
        // If we passed Redis, we are allowed to enter the database transaction.
        // We still use Locking for safety, but traffic is now throttled by Redis.
        Event event = eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getAvailableTickets() > 0) {
            // Artificial delay to prove Redis is doing its job
            try { Thread.sleep(50); } catch (InterruptedException e) {}

            event.setAvailableTickets(event.getAvailableTickets() - 1);
            eventRepository.save(event);

            Ticket ticket = new Ticket(userId, eventId);
            ticketRepository.save(ticket);

            return "✅ Success!";
        } else {
            return "❌ DB says: Sold out.";
        }
    }

    public String getEventStatus(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return "Event: " + event.getName() + " | Tickets Left: " + event.getAvailableTickets();
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }

    public long countSoldTickets(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }
}