# 🎫 TicketBlitz - High-Concurrency Distributed Booking System

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-green)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Postgres](https://img.shields.io/badge/Postgres-16-blue)
![Redis](https://img.shields.io/badge/Redis-7-red)
![Kafka](https://img.shields.io/badge/Kafka-7.6-black)

## 📖 The Problem
In standard ticket booking systems, a "Race Condition" occurs when thousands of users try to buy the last ticket simultaneously. Without proper handling, multiple users can "buy" the same seat, leading to **Double Booking** and data inconsistency.

**TicketBlitz** is a distributed system engineered to handle these high-concurrency scenarios using Optimistic Locking, Distributed Caching (Redis), and Event-Driven Architecture (Kafka).

---

## 🏗 System Architecture (Planned)
The system evolves through 4 stages of complexity:

1.  **Phase 1 (Naive):** A standard REST API connected to PostgreSQL (Vulnerable to Race Conditions).
2.  **Phase 2 (The Fix):** Implementing **Optimistic Locking (`@Version`)** to prevent data corruption.
3.  **Phase 3 (Scale):** Adding **Redis** for distributed locking to reduce database load.
4.  **Phase 4 (Async):** Decoupling bookings using **Apache Kafka** to handle massive traffic spikes (10k+ TPS).

---

## 🛠 Tech Stack
* **Core:** Java 25 (Virtual Threads), Spring Boot 3.4
* **Database:** PostgreSQL 16
* **Caching & Locking:** Redis (Alpine)
* **Message Broker:** Apache Kafka (KRaft mode)
* **Infrastructure:** Docker & Docker Compose

---

## 🚀 Getting Started

### Prerequisites
* Docker Desktop (Running)
* Java 25 SDK

### 1. Infrastructure Setup
Start the Database, Cache, and Message Broker in isolated containers:
```bash
docker compose up -d