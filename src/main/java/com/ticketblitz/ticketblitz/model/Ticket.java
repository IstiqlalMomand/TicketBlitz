package com.ticketblitz.ticketblitz.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long eventId;

    private LocalDateTime bookedAt;

    // Constructor helper
    public Ticket(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
        this.bookedAt = LocalDateTime.now();
    }
}