package com.ticketblitz.ticketblitz;

import com.ticketblitz.ticketblitz.model.Event;
import com.ticketblitz.ticketblitz.repository.EventRepository;
import com.ticketblitz.ticketblitz.service.RedisService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;
    private final RedisService redisService; // ✅ Inject Redis

    public DataInitializer(EventRepository eventRepository, RedisService redisService) {
        this.eventRepository = eventRepository;
        this.redisService = redisService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (eventRepository.count() == 0) {
            Event event = new Event();
            event.setName("Taylor Swift Concert");
            event.setTotalTickets(100);
            event.setAvailableTickets(100);

            eventRepository.save(event);

            // ✅ Sync to Redis
            redisService.setTicketCount(event.getId(), 100);

            System.out.println("---------------------------------------------");
            System.out.println("🚀 TEST DATA CREATED: 100 tickets in DB & Redis");
            System.out.println("---------------------------------------------");
        }
    }
}