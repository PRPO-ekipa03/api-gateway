# API Gateway Microservice

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Setup and Installation](#setup-and-installation)
  - [Prerequisites](#prerequisites)
  - [Clone the Repository](#clone-the-repository)
  - [Build the Project](#build-the-project)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
  - [Locally](#locally)
  - [Using Docker](#using-docker)
- [API Gateway Routes](#api-gateway-routes)
- [Security Configuration](#security-configuration)
- [CORS Configuration](#cors-configuration)

---

## Overview
The **API Gateway** acts as a single entry point for routing requests to various microservices, providing security, routing, and CORS configurations.

---

## Features
- Centralized API routing for all services.
- JWT-based authentication for secured endpoints.
- Custom security filter for token validation.
- Flexible CORS configuration for frontend compatibility.
- Detailed logging for debugging purposes.

---

## Technologies Used
- **Java 21** with **Spring Boot**
- **Spring Security** for authentication and authorization
- **Spring Cloud Gateway** for routing
- **OpenAPI/Swagger** for documentation
- **Docker** for containerization
- **Maven** for build management

---

## Architecture
The API Gateway uses the following components:
- **Gateway Routes**: Define paths for routing requests to backend microservices.
- **Security Configuration**: Handles authentication and authorization.
- **CORS Configuration**: Ensures cross-origin compatibility.
- **JWT Filter**: Validates tokens with the authentication service.

---

## Setup and Installation

### Prerequisites
- Java Development Kit (JDK) 21
- Maven 3.9+
- Docker (optional)

### Clone the Repository
``bash
git clone https://github.com/PRPO-ekipa03/api-gateway.git
cd api-gateway
``

### Build the Project
``bash
mvn clean package -DskipTests
``

---

## Configuration
Set the following configuration parameters in your environment or `application.yml` file:

``yaml
server:
  port: 8080
  forward-headers-strategy: framework

spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      mvc:
        routes:
          - id: user_service
            uri: http://${USER_SERVICE_HOST:localhost}:${USER_SERVICE_PORT:8081}
            predicates:
              - Path=/api/users/**, /api/auth/**, /users/swagger-ui/**, /users/api-docs/**

          - id: payment_service
            uri: http://${PAYMENT_SERVICE_HOST:localhost}:${PAYMENT_SERVICE_PORT:8082}
            predicates:
              - Path=/api/payments/**, /payments/swagger-ui/**, /payments/api-docs/**

          - id: event_service
            uri: http://${EVENT_SERVICE_HOST:localhost}:${EVENT_SERVICE_PORT:8083}
            predicates:
              - Path=/api/events/**, /events/swagger-ui/**, /events/api-docs/**

          - id: venue_service
            uri: http://${VENUE_SERVICE_HOST:localhost}:${VENUE_SERVICE_PORT:8084}
            predicates:
              - Path=/api/venues/**, /api/reservations/**, /venues/swagger-ui/**, /venues/api-docs/**

          - id: guest_service
            uri: http://${GUEST_SERVICE_HOST:localhost}:${GUEST_SERVICE_PORT:8085}
            predicates:
              - Path=/api/guests/**, /guests/swagger-ui/**, /guests/api-docs/**

          - id: notification_service
            uri: http://${NOTIFICATION_SERVICE_HOST:localhost}:${NOTIFICATION_SERVICE_PORT:8086}
            predicates:
              - Path=/api/notifications/**, /notifications/swagger-ui/**, /notifications/api-docs/**

logging:
  level:
    org:
      springframework:
        cloud:
          gateway: DEBUG
        web: DEBUG
        security:
          web: DEBUG
``

---

## Running the Application

### Locally
Run the application with:
``bash
java -jar target/api-gateway.jar
``  

The service will start on port 8080 or the port specified in your configuration.

### Using Docker
To containerize the application:

1. **Build the Docker Image**:  
``bash
docker build -t api-gateway .
``  

2. **Run the Docker Container**:  
``bash
docker run -p 8080:8080 \
  -e USER_SERVICE_HOST=your-user-service-host \
  -e USER_SERVICE_PORT=your-user-service-port \
  -e PAYMENT_SERVICE_HOST=your-payment-service-host \
  -e PAYMENT_SERVICE_PORT=your-payment-service-port \
  -e EVENT_SERVICE_HOST=your-event-service-host \
  -e EVENT_SERVICE_PORT=your-event-service-port \
  -e VENUE_SERVICE_HOST=your-venue-service-host \
  -e VENUE_SERVICE_PORT=your-venue-service-port \
  -e GUEST_SERVICE_HOST=your-guest-service-host \
  -e GUEST_SERVICE_PORT=your-guest-service-port \
  -e NOTIFICATION_SERVICE_HOST=your-notification-service-host \
  -e NOTIFICATION_SERVICE_PORT=your-notification-service-port \
  api-gateway
``  

---

## API Gateway Routes
The API Gateway routes requests to various microservices based on the path:

- **User Service**: `/api/users/**`, `/api/auth/**`
- **Payment Service**: `/api/payments/**`
- **Event Service**: `/api/events/**`
- **Venue Service**: `/api/venues/**`, `/api/reservations/**`
- **Guest Service**: `/api/guests/**`
- **Notification Service**: `/api/notifications/**`

---

## Security Configuration
The gateway uses a **JWT Filter** to authenticate incoming requests. The filter:
- Extracts the token from the `Authorization` header.
- Validates the token with the **User Service**.
- Adds the `X-User-Id` header to the request for downstream services.

---

## CORS Configuration
CORS allows the frontend to interact with the API Gateway:
- **Allowed Origins**: Set via `FRONTEND_HOST` (default: `http://localhost:4200`).
- **Allowed Methods**: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`.
- **Allowed Headers**: All headers are permitted.
- **Exposed Headers**: `Authorization` is exposed to the frontend.
