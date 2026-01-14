# 🚚 SmartLoad Optimizer

SmartLoad Optimizer is a **Spring Boot** microservice that helps trucking companies maximize load efficiency by selecting the most profitable combination of orders while respecting truck capacity, hazmat restrictions, and delivery constraints.

---

## ✨ Features

- Optimize truck loads based on **weight, volume, and payout**
- Support for **hazmat restrictions** and delivery windows
- RESTful API endpoint:
    - `POST /api/v1/load-optimizer/optimize`
- Built with **Java 17**, **Spring Boot 3.5.x**, and **Maven**
- Includes **Spring Boot Actuator** for health monitoring
- Docker-ready for containerized deployment

---

## 🚀 Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 17 & Maven 3.9+ (optional)

---

## 🐳 Docker Usage

### Build & Run with Docker Compose

```bash
docker compose up --build
```

### Stop the Service

```bash
docker compose down
```

---

### 🛠 Local Build (Optional)

Build the project using Maven:

```bash
mvn clean package
```

Run the application locally:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

The service will be available at:

```
http://localhost:8080
```

---

## 🔍 Testing the API

### Sample Request

A sample JSON payload is available at:

```
src/main/resources/static/sample-request.json
```

Send the request using `curl`:

```bash
curl -X POST http://localhost:8080/api/v1/load-optimizer/optimize \
  -H "Content-Type: application/json" \
  -d @src/main/resources/static/sample-request.json
```

---

### Example Response

```json
{
  "truck_id": "truck-123",
  "selected_order_ids": ["ord-001", "ord-002"],
  "total_payout_cents": 430000,
  "total_weight_lbs": 30000,
  "total_volume_cuft": 2100,
  "utilization_weight_percent": 68.18,
  "utilization_volume_percent": 70.0
}
```

---

## ✅ Health Check

Spring Boot Actuator is enabled. You can verify the service health using:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

---

## 🧰 Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Web
- Spring Validation
- Spring Boot Actuator
- Lombok
- Maven

---
