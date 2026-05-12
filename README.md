# Incident Management API

A backend monitoring and incident management service built with Java and Spring Boot.

This project allows users to register health-check monitors for external services, run scheduled checks against those endpoints, and automatically log incidents when failures occur.

---

## 🚀 Features

### Monitor Management
Create and manage monitors with configurable:

- Name
- URL
- Expected HTTP status code
- Check interval (seconds)
- Callback URL
- Active / inactive status

### Automated Health Checks

A scheduled worker polls the database for monitors due to run and dispatches checks asynchronously.

Each monitor request:

- Executes an HTTP GET request
- Compares actual vs expected status
- Supports retry logic for transient network failures
- Runs in parallel so slow endpoints do not block others

### Incident Logging

Incidents are automatically created when:

- Returned HTTP status does not match expected status
- Endpoint times out / connection fails after retries

Each incident stores:

- Monitor reference
- URL
- Failure type
- Expected status
- Actual status (if available)
- Timestamp

---

## 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Scheduling
- Spring Async
- Spring Retry
- PostgreSQL
- Flyway
- MapStruct
- Lombok
- Swagger / OpenAPI

---

## 🧱 Architecture

```text
Scheduler
   ↓
Fetch due monitors
   ↓
Async execution
   ↓
HTTP health check
   ↓
Retry transient failures
   ↓
Create incident on failure
