# 🖥 Backend – Message Router

Spring Boot backend for Message Router System.

## 🔹 Features

- REST APIs for messages and partners
- IBM MQ integration
- PostgreSQL database
- JWT Authentication (Keycloak)
- Error handling & retry logic
- Containerized with Docker

## 🐳 Example Datasource Configuration (application.yml.example)

spring:
  datasource:
    # Example using H2 for testing
    url: jdbc:h2:file:./data/message_db
    driver-class-name: org.h2.Driver
    username: your_username
    password: your_password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

## 🐳 Run Backend with Docker

```bash
docker build -t message-router-backend .
docker run -p 8080:8080 message-router-backend


## 👨‍💻 Author

Youssef Jmaiel  
Software Engineer – Spring Boot & Angular Developer
