# Message Router Backend

![Java](https://img.shields.io/badge/Java-11-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-2.x-green)
![Keycloak](https://img.shields.io/badge/Auth-Keycloak-orange)
![IBM MQ](https://img.shields.io/badge/Messaging-IBM%20MQ-red)
![Docker](https://img.shields.io/badge/Container-Docker-blue)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

Backend service for a **Banking Message Routing System** built using **Spring Boot**.
The backend handles messages between systems via **IBM MQ**, stores them in a database, and provides **secure REST APIs** for managing messages and partners.

The project demonstrates enterprise backend architecture:

* Middleware integration with IBM MQ
* OAuth2 authentication via Keycloak
* Role-based authorization
* Pagination & sorting
* Global exception handling
* Unit testing and validation
* Dockerized deployment

---

## Features

* IBM MQ messaging integration (send and receive)
* Message management and storage
* Partner management
* Secure REST APIs with Keycloak
* Pagination and sorting support
* Global exception handling
* DTO validation
* Unit testing (JUnit + Mockito)
* Docker-ready setup
* Configurable via `application.yml.example`

---

## Tech Stack

* Java 11
* Spring Boot 2.x
* Spring Data JPA
* Spring Security
* OAuth2 Resource Server
* Keycloak
* IBM MQ
* PostgreSQL / H2
* Maven
* Docker
* JUnit & Mockito

---

## Architecture Overview

```
                +-------------+
                |   Keycloak  |
                | Authentication |
                +------+------+
                       |
                +------v------+
                | Spring Boot |
                | Message Router |
                +------+------+
                       |
        +--------------+-------------+
        |                            |
  +-----v-----+               +------v------+
  |  IBM MQ   |               | PostgreSQL  |
  | Messaging |               | Database    |
  +-----------+               +-------------+
```

---

## Project Structure

```
message-router-backend
│
├── config
│   ├── GlobalExceptionHandler
│   ├── JacksonConfig
│   ├── MqConfig
│   ├── MqConfigProperties
│   └── SecurityConfig
│
├── controller
│   ├── MessageController
│   └── PartnerController
│
├── service
│   ├── MessageService
│   ├── PartnerService
│   ├── MqService
│   └── MqMessageListener
│
├── repository
│   ├── MessageRepository
│   └── PartnerRepository
│
├── entity
│   ├── Message
│   └── Partner
│
├── dto
│   └── MessageRequest
│
├── error
│   ├── ErrorResponse
│   ├── MessageNotFoundException
│   └── PartnerNotFoundException
│
└── MessageRouterApplication
```

---

## Message API Endpoints

* **GET /api/messages** – Retrieve all messages
* **GET /api/messages/paged?page=0&size=10&sortBy=timestamp&direction=desc** – Paginated messages
* **GET /api/message/{id}** – Get message by ID
* **POST /api/message** – Create message
* **DELETE /api/message/{id}** – Delete message
* **POST /api/message/send** – Send a message

Example JSON for sending a message:

```json
{
  "content": "Test message",
  "sender": "SYSTEM_A",
  "receiver": "SYSTEM_B"
}
```

---

## Partner API Endpoints

* **GET /api/partners** – Retrieve all partners
* **GET /api/partners/paged** – Paginated partner list
* **GET /api/partners/{id}** – Get partner by ID
* **POST /api/partners** – Create partner
* **DELETE /api/partners/{id}** – Delete partner

---

## Security

OAuth2 authentication via Keycloak is implemented.

Role-based access:

* **ADMIN** – access message APIs
* **USER** – access partner APIs

Include the token in your request header:

```http
Authorization: Bearer <access_token>
```

---

## IBM MQ Integration

### Producer

Send messages to the MQ queue with `MqService.sendMessage(String message)`.

### Consumer / Listener

Consume messages asynchronously from the MQ queue with `MqMessageListener`:

```java
@Component
@Slf4j
public class MqMessageListener {
    public void handleMessage(String message) {
        log.info("Received message from MQ: {}", message);
    }
}
```

**Benefits**:

* Asynchronous messaging
* Reliable delivery
* Decoupled architecture
* High scalability

---

## Configuration

Sensitive info is excluded from Git.
Use `application.yml.example` as a template:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/message_router
    username: postgres
    password: password

mq:
  host: localhost
  port: 1414
  queue: MESSAGE.QUEUE

keycloak:
  realm: spring-app
  resource: spring-boot-client
```

---

## Running the Project

Build:

```bash
mvn clean install
```

Run:

```bash
mvn spring-boot:run
```

The app runs on:

```
http://localhost:8080
```

---

## Unit Testing

Covers **service and controller layers** using JUnit + Mockito.

```bash
mvn test
```

---

## Purpose

This backend simulates a **real banking message routing system**, demonstrating:

* Enterprise-grade Spring Boot backend
* IBM MQ messaging integration
* Secure REST APIs with OAuth2
* Role-based authorization
* Pagination & sorting
* Exception handling
* Backend unit testing
