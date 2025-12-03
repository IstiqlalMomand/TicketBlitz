package com.ticketblitz.ticketblitz;

import com.ticketblitz.ticketblitz.model.Event;
import com.ticketblitz.ticketblitz.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    public DataInitializer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Check if we already have data
        if (eventRepository.count() == 0) {

            // 2. Create the Event
            Event event = new Event();
            event.setName("Taylor Swift Concert");
            event.setTotalTickets(100);
            event.setAvailableTickets(100);

            // 3. Save to Database
            eventRepository.save(event);

            System.out.println("---------------------------------------------");
            System.out.println("🚀 TEST DATA CREATED: 'Taylor Swift Concert' with 100 tickets");
            System.out.println("---------------------------------------------");
        } else {
            System.out.println("✅ Database already has data.");
        }
    }
}