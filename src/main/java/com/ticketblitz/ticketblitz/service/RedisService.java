package com.ticketblitz.ticketblitz.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Set the initial counter (e.g., "event:1:tickets" -> 100)
    public void setTicketCount(Long eventId, int count) {
        String key = "event:" + eventId + ":tickets";
        redisTemplate.opsForValue().set(key, String.valueOf(count));
    }

    // Atomically decrease the counter.
    // Returns the NEW value. If it returns -1, it means we just went below 0.
    public Long decrementTicketCount(Long eventId) {
        String key = "event:" + eventId + ":tickets";
        return redisTemplate.opsForValue().decrement(key);
    }
}