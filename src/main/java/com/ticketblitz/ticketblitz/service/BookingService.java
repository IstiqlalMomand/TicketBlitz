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

    public BookingService(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public String bookTicket(Long eventId, Long userId) {
        // USE THE LOCK!
        Event event = eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // 2. CHECK: Are tickets available?
        if (event.getAvailableTickets() > 0) {

            // ⚠️ ARTIFICIAL DELAY to force Race Condition
            try { Thread.sleep(50); } catch (InterruptedException e) {}

            // 3. ACT: Reduce the count
            event.setAvailableTickets(event.getAvailableTickets() - 1);
            eventRepository.save(event);

            // 4. Create the Ticket receipt
            Ticket ticket = new Ticket(userId, eventId);
            ticketRepository.save(ticket);

            return "✅ Success! Ticket booked for User " + userId;
        } else {
            return "❌ Failed! Sold out.";
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