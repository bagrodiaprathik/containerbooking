# Maersk Container Booking API

A reactive, non-blocking API for booking containers, built with **Spring Boot**, **WebFlux**, and **MongoDB**.

---

## Features

- **Reactive Stack:** Fully non-blocking from controller (WebFlux) to database (ReactiveMongoRepository).
- **Database Sequence:** Custom atomic counter generates sequential, human-readable booking references (e.g., `957000001`).
- **Validation:** Uses standard `jakarta.validation` and custom annotations for request body validation.
- **Error Handling:** Global exception handler returns standardized error messages.
- **TDD:** Full suite of unit and integration tests.
- **Testcontainers:** All database-related tests run against a real, temporary MongoDB instance managed by Testcontainers.

---

## Tech Stack

- Java 17
- Spring Boot 3.3.1
- Spring WebFlux (Reactive Web)
- Spring Data MongoDB Reactive
- Project Reactor (Mono, Flux)
- MongoDB
- Gradle (Build Tool)
- Lombok

**Testing:**
- JUnit 5
- Testcontainers (MongoDB integration tests)
- MockWebServer (mocking external API)
- reactor-test (StepVerifier)

---

## Prerequisites

- Java 17 JDK
- Docker Desktop (must be running for local database)
- Git (optional, for cloning)
- API Client (Postman, Insomnia, etc.)

---

## Getting Started

### 1. Run the Database

Start a MongoDB instance using Docker:

```sh
docker run --name maersk-mongo -p 27017:27017 -d mongo:latest
```

- `--name maersk-mongo`: Memorable container name.
- `-p 27017:27017`: Maps your computer's port 27017 to the container's port.
- `-d`: Runs the container in detached mode.

Verify the container is running:

```sh
docker ps
```

---

### 2. Run the Application

Once the database is running, start the application using Gradle:

**Windows:**
```sh
.\gradlew.bat bootRun
```

**macOS/Linux:**
```sh
./gradlew bootRun
```

The API will be available at [http://localhost:8080](http://localhost:8080).

---

## Running the Tests

Tests use Testcontainers to start/stop a temporary MongoDB instance. You do **not** need your local database running to run tests.

**Windows:**
```sh
.\gradlew.bat test
```

**macOS/Linux:**
```sh
./gradlew test
```

You will see a `BUILD SUCCESSFUL` message if all tests pass.

---

## API Endpoints

### 1. Check Availability

Checks if container space is available (simulates external API call).

- **Method:** `POST`
- **URL:** `/api/bookings/check-availability`
- **Request Body:**
  ```json
  {
    "containerType": "DRY",
    "containerSize": 20,
    "origin": "Southampton",
    "destination": "Singapore",
    "quantity": 5
  }
  ```
- **Response (Success):**
  ```json
  {
    "available": true
  }
  ```
- **Note:** The external service at `https://maersk.com/api/bookings/checkAvailable` doesn't exist. The service logs an error and returns `{"available": false}` as per requirements.

---

### 2. Create Booking

Creates and saves a new container booking.

- **Method:** `POST`
- **URL:** `/api/bookings`
- **Request Body:**
  ```json
  {
    "containerType": "DRY",
    "containerSize": 20,
    "origin": "Southampton",
    "destination": "Singapore",
    "quantity": 5,
    "timestamp": "2024-10-12T13:53:09Z"
  }
  ```
- **Response (Success):**
  ```json
  {
    "bookingRef": "957000001"
  }
  ```
- **Response (Server Error):**
  ```json
  {
    "message": "Sorry there was a problem processing your request"
  }
  ```

---

## Validation Rules

| Field         | Constraints                                      |
|---------------|--------------------------------------------------|
| containerSize | Not-null. Must be 20 or 40.                      |
| containerType | Not-null. Must be DRY or REEFER.                 |
| origin        | Not-blank. 5–20 characters.                      |
| destination   | Not-blank. 5–20 characters.                      |
| quantity      | Not-null. 1–100.                                 |
| timestamp     | (Create Booking only) Not-blank, valid ISO-8601. |

---


