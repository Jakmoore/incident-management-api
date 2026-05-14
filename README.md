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
- Callback email
- Active / inactive status

### Automated Health Checks

A scheduled worker polls the database for monitors due to run and dispatches checks asynchronously.

Each monitor request:

- Executes an HTTP GET request
- Compares actual vs expected status
- Supports retry logic for transient network failures
- Runs in parallel so slow endpoints do not block others

### Manual Health Check Execution

Monitors can also be executed manually via API endpoints for immediate verification or troubleshooting.

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
- Fingerprint
- Open incident flag

---

## 📡 API Endpoints

### Current Endpoints

#### Monitor Administration

Base path: `/api/admin/monitors`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admin/monitors` | Create a new monitor |
| GET | `/api/admin/monitors` | Retrieve all monitors |
| GET | `/api/admin/monitors/{id}` | Retrieve monitor by ID |
| PATCH | `/api/admin/monitors/{id}/enable` | Enable monitor |
| PATCH | `/api/admin/monitors/{id}/disable` | Disable monitor |

#### Manual Monitor Execution

Base path: `/api/monitors`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/monitors/{monitorId}` | Manually execute health check |

#### Incident Retrieval

Base path: `/api/incidents`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/incidents/{monitorId}` | Retrieve incidents for a monitor |

---

### Planned Endpoints

These are intended future enhancements.

#### Monitor Administration

| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/api/admin/monitors/{id}` | Update monitor configuration |
| DELETE | `/api/admin/monitors/{id}` | Delete monitor |

#### Manual Execution

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/monitors/run-all` | Trigger all active monitors |

#### Incident Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/incidents` | Retrieve all incidents |
| GET | `/api/incidents/open` | Retrieve open incidents |
| PATCH | `/api/incidents/{id}/resolve` | Resolve incident manually |

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
Generate health check result
   ↓
Raise notification + create incident
