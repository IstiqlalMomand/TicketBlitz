package com.ticketblitz.ticketblitz.dto;

public class BookingRequest {
    private Long userId;
    private Long eventId;

    // Constructors
    public BookingRequest() {}
    public BookingRequest(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    // Getters
    public Long getUserId() { return userId; }
    public Long getEventId() { return eventId; }
}