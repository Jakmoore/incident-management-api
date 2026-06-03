# 🚨 Incident Management API

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)

A backend system for monitoring external services, running automated health checks, and tracking failures through
structured incident reporting.

It provides both scheduled and manual execution of checks, with automatic detection and logging of service degradation
or outages.

---

## ✨ Features

### 📡 Monitor Management

Users can define and manage monitors that represent external services to be tracked.

Each monitor includes:

- 🏷️ Name
- 🌐 Target URL
- 🎯 Expected HTTP status code
- ⏱️ Check interval (seconds)
- 📧 Callback email notifications
- 🔄 Active / inactive state

Monitors are persisted using a relational database with Spring Data JPA.

---

### ⚙️ Automated Health Checks

A background scheduler continuously evaluates which monitors are due for execution.

This subsystem uses Spring Scheduling for orchestration and Spring Async for parallel execution, allowing high
throughput without blocking.

Each check:

- 🌍 Sends HTTP GET requests to external services
- ✅ Validates responses against expected status codes
- 🔁 Retries transient failures using Spring Retry
- ⚡ Runs concurrently for scalability
- 🐘 Stores results in PostgreSQL via JPA

---

### 🧪 Manual Health Checks

Provides an on-demand endpoint for executing a monitor instantly.

This uses the same internal service layer as the scheduler to ensure consistent behaviour between manual and automated
execution paths.

---

### 🚨 Incident Logging

When a failure is detected, the system automatically creates an incident record.

Failures include:

- ❌ Unexpected HTTP status codes
- ⏳ Timeout or connection failures after retries

Each incident contains:

- 🔗 Monitor reference
- 🌐 Target URL
- ⚠️ Failure type
- 🎯 Expected status code
- 📉 Actual status code (if available)
- 🕒 Timestamp
- 🧬 Fingerprint for deduplication
- 🔓 Open / resolved state

Incident data is persisted in PostgreSQL via Spring Data JPA with transactional guarantees.

---

## 📡 API Endpoints

---

## 🛠 Admin Monitor Management

Base path: `/api/admin/monitors`

| Method | Endpoint                           | Description                  |
|--------|------------------------------------|------------------------------|
| POST   | `/api/admin/monitors`              | ➕ Create a new monitor       |
| GET    | `/api/admin/monitors`              | 📋 Retrieve all monitors     |
| GET    | `/api/admin/monitors/{id}`         | 🔍 Retrieve monitor by ID    |
| PUT    | `/api/admin/monitors/{id}`         | ✏️ Update monitor config     |
| PATCH  | `/api/admin/monitors/{id}/enable`  | 🟢 Enable monitor            |
| PATCH  | `/api/admin/monitors/{id}/disable` | 🔴 Disable monitor           |
| DELETE | `/api/admin/monitors/{id}`         | 🗑️ Delete monitor           |
| POST   | `/api/admin/monitors/run-all`      | 🚀 Trigger all active checks |

All operations are handled through a Spring Boot service layer backed by JPA persistence.

---

## 🧪 Manual Monitor Execution

Base path: `/api/monitors`

| Method | Endpoint                    | Description               |
|--------|-----------------------------|---------------------------|
| GET    | `/api/monitors/{monitorId}` | ⚡ Run manual health check |

This endpoint executes the same internal service logic used by scheduled jobs.

---

## 🚨 Incident Retrieval

Base path: `/api/incidents`

| Method | Endpoint                                   | Description                        |
|--------|--------------------------------------------|------------------------------------|
| GET    | `/api/incidents/{monitorId}`               | 📊 Get all incidents for a monitor |
| GET    | `/api/incidents/{monitorId}?openOnly=true` | 🔓 Get only open incidents         |

Incidents are stored in PostgreSQL and accessed via Spring Data JPA repositories.

---

## 🛠 Admin Incident Management

Base path: `/api/admin/incidents`

| Method | Endpoint                            | Description                 |
|--------|-------------------------------------|-----------------------------|
| GET    | `/api/admin/incidents`              | 📋 Retrieve all incidents   |
| GET    | `/api/admin/incidents/open`         | 🔓 Retrieve open incidents  |
| PATCH  | `/api/admin/incidents/{id}/resolve` | ✅ Resolve incident manually |
| DELETE | `/api/admin/incidents/{id}`         | 🗑️ Delete incident         |

---

## 📈 Metrics API

Base path: `/api/metrics/monitors`

| Method | Endpoint                            | Description                                                        |
|--------|-------------------------------------|--------------------------------------------------------------------|
| GET    | `/api/metrics/monitors/{monitorId}` | 📊 Retrieve monitor metrics (query param: `cutoff`, default `30d`) |

Metrics are derived from historical check results stored in PostgreSQL and aggregated through a service layer.

---

## 🧠 Architecture Overview

```text
🕒 Scheduler triggers execution cycle
   ↓
📥 JPA fetches due monitors
   ↓
⚡ Async workers execute checks in parallel
   ↓
🌐 HTTP requests sent to external services
   ↓
🔁 Retry layer handles transient failures
   ↓
📊 Results aggregated into metrics
   ↓
🚨 Incidents created and persisted in PostgreSQL