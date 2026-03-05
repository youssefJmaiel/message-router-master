# Message Router Backend

Backend service for a **Banking Message Routing System** built with Spring Boot.
The application integrates with **IBM MQ**, stores messages in a database, and exposes secure REST APIs to manage messages and partners.

The system includes **pagination, role-based security with Keycloak, exception handling, and unit testing**.

---

# Features

* IBM MQ integration (send and receive messages)
* Message storage and management
* Partner management
* Secure REST APIs
* Pagination and sorting
* Role-based access control (Keycloak)
* Exception handling
* Unit testing (Service and Controller)
* Docker ready
* Secure configuration with `application.yml.example`

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
* JUnit / Mockito

---

# Project Structure

src/main/java/com/bankapp/messagerouter

controller

* MessageController
* PartnerController

service

* MessageService
* PartnerService
* MqService

repository

* MessageRepository
* PartnerRepository

entity

* Message
* Partner

dto

* MessageRequest

error

* MessageNotFoundException
* PartnerNotFoundException

---

# MessageController

Handles REST endpoints for message operations.

Endpoints:

GET /api/messages
Retrieve all messages (ADMIN role)

GET /api/messages/paged
Retrieve paginated messages

GET /api/message/{id}
Retrieve message by ID

POST /api/message
Save a new message

DELETE /api/message/{id}
Delete message

POST /api/message/send
Send a message using MessageRequest DTO

Example request body:

{
"content": "Test message",
"sender": "SYSTEM_A",
"receiver": "SYSTEM_B"
}

---

# PartnerController

Handles partner management APIs.

Endpoints:

GET /api/partners
Retrieve all partners

GET /api/partners/paged
Retrieve paginated partners

GET /api/partners/{id}
Retrieve partner by ID

POST /api/partners
Create new partner

DELETE /api/partners/{id}
Delete partner

---

# Security

The application is secured using **OAuth2 with Keycloak**.

Role-based access:

ADMIN

* Access messages endpoints

USER

* Access partner endpoints

Example Authorization header:

Authorization: Bearer <access_token>

---

# IBM MQ Integration

The application integrates with IBM MQ using **JmsTemplate**.

Capabilities:

* Connect to MQ queue
* Send messages
* Receive messages

Example operations:

sendMessage(String message)

receiveMessage()

---

# Configuration

Sensitive configuration is not committed to Git.

The project provides:

application.yml.example

Developers must copy it:

cp application.yml.example application.yml

Example configuration:

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

---

# Running the Project

Build project

mvn clean install

Run application

mvn spring-boot:run

Application runs on:

http://localhost:8080

---

# Unit Testing

The project includes unit tests for:

Service layer
Controller layer

Technologies used:

* JUnit
* Mockito
* Spring Boot Test

Run tests:

mvn test

---

# Purpose of the Project

This project simulates a **real enterprise banking backend system** that integrates with messaging middleware.

It demonstrates:

* Spring Boot backend development
* IBM MQ integration
* secure REST APIs
* role-based security with Keycloak
* pagination and sorting
* backend testing
