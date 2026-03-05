# Message Router Backend

![Java](https://img.shields.io/badge/Java-11-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-2.x-green)
![Keycloak](https://img.shields.io/badge/Auth-Keycloak-orange)
![IBM MQ](https://img.shields.io/badge/Messaging-IBM%20MQ-red)
![Docker](https://img.shields.io/badge/Container-Docker-blue)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

Backend service for a **Banking Message Routing System** built with **Spring Boot**.
The application integrates with **IBM MQ**, stores messages in a database, and exposes secure REST APIs to manage messages and partners.

The system demonstrates **enterprise backend architecture** including:

* Messaging middleware integration
* OAuth2 authentication with Keycloak
* Pagination and sorting
* Exception handling
* Unit testing

---

# Features

* IBM MQ integration (send and receive messages)
* Message storage and management
* Partner management
* RESTful APIs
* Pagination and sorting
* Role-based security with Keycloak
* Exception handling
* Unit testing (Service & Controller)
* Docker ready
* Secure configuration using `application.yml.example`

---

# Tech Stack

* Java 11
* Spring Boot
* Spring Data JPA
* Spring Security
* OAuth2 Resource Server
* Keycloak
* IBM MQ
* PostgreSQL / H2
* Maven
* Docker
* JUnit
* Mockito

---

# Architecture

```
                +-------------+
                |   Keycloak  |
                | Authentication |
                +------+------+
                       |
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

# Project Structure

```
message-router-backend
│
├── controller
│   ├── MessageController
│   └── PartnerController
│
├── service
│   ├── MessageService
│   ├── PartnerService
│   └── MqService
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
│   ├── MessageNotFoundException
│   └── PartnerNotFoundException
│
└── config
    └── SecurityConfig
```

---

# Message API

Retrieve all messages

GET /api/messages

Retrieve paginated messages

GET /api/messages/paged?page=0&size=10&sortBy=timestamp&direction=desc

Retrieve message by ID

GET /api/message/{id}

Create a message

POST /api/message

Delete message

DELETE /api/message/{id}

Send message

POST /api/message/send

Example request body

```
{
  "content": "Test message",
  "sender": "SYSTEM_A",
  "receiver": "SYSTEM_B"
}
```

---

# Partner API

Retrieve all partners

GET /api/partners

Retrieve paginated partners

GET /api/partners/paged?page=0&size=10&sortBy=alias&direction=asc

Retrieve partner by ID

GET /api/partners/{id}

Create partner

POST /api/partners

Delete partner

DELETE /api/partners/{id}

---

# Security

The application is secured using **OAuth2 with Keycloak**.

Role-based access:

ADMIN

* access message endpoints

USER

* access partner endpoints

Example Authorization header

```
Authorization: Bearer <access_token>
```

---

# IBM MQ Integration

The application integrates with **IBM MQ** using Spring JMS.

Capabilities:

* Connect to MQ queue
* Send messages
* Receive messages

Example operations

```
sendMessage(String message)

receiveMessage()
```

---

# Configuration

Sensitive configuration is not stored in Git.

Instead the project includes:

```
application.yml.example
```

Create your configuration:

```
cp application.yml.example application.yml
```

Example configuration

```
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

# Running the Project

Build the project

```
mvn clean install
```

Run the application

```
mvn spring-boot:run
```

Application runs on

```
http://localhost:8080
```

---

# Unit Testing

The project includes unit tests for:

* Service layer
* Controller layer

Technologies used:

* JUnit
* Mockito
* Spring Boot Test

Run tests

```
mvn test
```

---

# Purpose of the Project

This project simulates a **real enterprise banking backend system** that integrates with messaging middleware.

It demonstrates:

* Spring Boot backend development
* IBM MQ messaging integration
* Secure REST APIs
* OAuth2 authentication
* Role-based authorization
* Pagination and sorting
* Backend testing
* Containerized deployment
