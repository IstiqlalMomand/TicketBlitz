#  TicketBlitz - High-Concurrency Distributed Booking System

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-green)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Postgres](https://img.shields.io/badge/Postgres-16-blue)
![Redis](https://img.shields.io/badge/Redis-7-red)
![Kafka](https://img.shields.io/badge/Kafka-7.6-black)

##  The Problem
In standard ticket booking systems, a "Race Condition" occurs when thousands of users try to buy the last ticket simultaneously. Without proper handling, multiple threads read the same "available count" before anyone updates it, leading to **Double Booking** and massive data inconsistency (The "Lost Update" Anomaly).

**TicketBlitz** is a distributed system engineered to handle these high-concurrency scenarios using **Pessimistic Locking**, **Distributed Caching (Redis)**, and **Event-Driven Architecture (Kafka)**.

---

##  System Architecture
The system follows a high-performance **Event-Driven Architecture** designed to handle 10k+ TPS (Transactions Per Second).

```mermaid
sequenceDiagram
    participant User
    participant API as Spring Boot API
    participant Kafka as Apache Kafka
    participant Consumer as Kafka Consumer
    participant Redis as Redis Cache
    participant DB as PostgreSQL (DB)

    User->>API: POST /book-ticket (User 101)
    API->>Kafka: Publish Event "1:101"
    API-->>User: 202 Accepted (Processing)
    
    Note over API, User: Non-blocking Response (Fast)

    Kafka->>Consumer: Async Message Push
    Consumer->>Redis: Decrement Ticket Count?
    
    alt Redis Count < 0
        Redis-->>Consumer: REJECT (Sold Out)
        Consumer->>Consumer: Stop Processing
    else Redis Count >= 0
        Redis-->>Consumer: OK
        Consumer->>DB: SELECT ... FOR UPDATE (Lock Row)
        DB-->>Consumer: Row Locked
        Consumer->>DB: Update & Save Ticket
        Consumer->>DB: Commit Transaction
    end
