# WorkForge

> A production-oriented task management and collaboration backend built with Java and Spring Boot, featuring secure authentication, role-based access control, Redis caching, online presence tracking, event-driven notifications with Apache Kafka, API rate limiting, and Dockerized infrastructure.

## Overview

WorkForge is a backend system designed to manage task assignment and collaboration between Administrators, Managers, and Employees.

The project focuses on practical backend and SDE engineering concepts including:

- Secure authentication and authorization
- RESTful API design
- MySQL database persistence
- Redis caching
- Online presence tracking
- Apache Kafka event-driven architecture
- Asynchronous notification processing
- API rate limiting
- Transaction management
- JPA/Hibernate relationship handling
- Docker-based infrastructure
- Cache invalidation and consistency

The project is intentionally designed as a modular monolith while using Kafka for asynchronous event-driven communication between application components.

---

## Key Engineering Highlights

- 20+ REST APIs covering authentication, user management, task management, and notifications
- 3 user roles: Admin, Manager, and Employee
- JWT access-token and refresh-token authentication
- HTTP-only cookie-based token handling
- Role-based access control with Spring Security
- Redis caching across 4 application cache regions
- Redis-based online presence tracking
- Apache Kafka with 3 business event flows
- API rate limiting
- Transactional task-update workflows
- Dockerized Spring Boot, MySQL, Redis, and Kafka environment

---

# System Architecture

```text
                         ┌─────────────────────┐
                         │       Client        │
                         │                     │
                         └──────────┬──────────┘
                                    │
                                    │ HTTP
                                    ▼
                         ┌─────────────────────┐
                         │    Spring Boot      │
                         │      REST API       │
                         └──────────┬──────────┘
                                    │
                    ┌───────────────┼────────────────┐
                    │               │                │
                    ▼               ▼                ▼
             ┌───────────┐   ┌────────────┐   ┌─────────────┐
             │  Security │   │  Business  │   │ Rate Limit  │
             │ JWT / RBAC│   │   Logic    │   │   Filter    │
             └───────────┘   └─────┬──────┘   └─────────────┘
                                   │
                    ┌──────────────┼───────────────┐
                    │              │               │
                    ▼              ▼               ▼
              ┌──────────┐   ┌──────────┐   ┌──────────┐
              │  MySQL   │   │  Redis   │   │  Kafka   │
              │ Database │   │ Cache &  │   │  Events  │
              │          │   │ Presence  │   │          │
              └──────────┘   └──────────┘   └────┬─────┘
                                                  │
                                                  ▼
                                         ┌────────────────┐
                                         │ Kafka Consumer │
                                         │ Notification   │
                                         │   Processing   │
                                         └───────┬────────┘
                                                 │
                                                 ▼
                                         ┌────────────────┐
                                         │ Notification   │
                                         │    Storage     │
                                         └────────────────┘