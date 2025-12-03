package com.ticketblitz.ticketblitz.repository;

import com.ticketblitz.ticketblitz.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByEventId(Long eventId);
}