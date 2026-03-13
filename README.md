# CTSE Lab 06 - Event-Driven Microservices (Spring Boot + Kafka)

This repository contains a simple event-driven microservices system built with Spring Boot and Apache Kafka.

When an order is created, `order-service` publishes an event to Kafka (`order-topic`).
Both `inventory-service` and `billing-service` consume that event and process it independently.

## Architecture

Flow:

1. Client sends `POST /orders` request to `api-gateway`.
2. `api-gateway` routes the request to `order-service`.
3. `order-service` publishes an event to Kafka topic `order-topic`.
4. `inventory-service` consumes the event and updates stock.
5. `billing-service` consumes the event and generates invoice logs.

## Services and Ports

| Service | Port | Responsibility |
|---|---:|---|
| `api-gateway` | `8080` | Entry point, routes `/orders/**` to `order-service` |
| `order-service` | `8081` | Accepts orders and publishes Kafka events |
| `inventory-service` | `8082` | Consumes order events and updates inventory |
| `billing-service` | `8083` | Consumes order events and handles billing |

Kafka bootstrap server is configured as `localhost:9092`.

## Prerequisites

- Java 17
- Docker Desktop (recommended for Kafka) or a local Kafka installation
- Internet connection for Maven dependency download on first run

## Run Kafka Locally

Start Kafka so it is reachable at `localhost:9092` before running services.

You can use any Kafka setup you prefer. If you already have Kafka running, skip this step.

## Start All Services

Open 4 terminals from the project root and run each service:

### Terminal 1

```powershell
cd .\api-gateway
.\mvnw spring-boot:run
```

### Terminal 2

```powershell
cd .\order-service
.\mvnw spring-boot:run
```

### Terminal 3

```powershell
cd .\inventory-service
.\mvnw spring-boot:run
```

### Terminal 4

```powershell
cd .\billing-service
.\mvnw spring-boot:run
```

## Test the Flow

Sample request body is available in `example-order-request.json`.

### PowerShell

```powershell
Invoke-RestMethod `
	-Method Post `
	-Uri "http://localhost:8080/orders" `
	-ContentType "application/json" `
	-InFile ".\example-order-request.json"
```

### cURL

```bash
curl -X POST "http://localhost:8080/orders" \
	-H "Content-Type: application/json" \
	-d "@example-order-request.json"
```

Expected API response:

```text
Order Created & Event Published
```

Expected logs:

- `inventory-service`: `Inventory Service Received Order ...` and `Stock Updated`
- `billing-service`: `Billing Service Received Order ...` and `Invoice Generated`

## API

### Create Order

- Method: `POST`
- URL: `http://localhost:8080/orders`
- Content-Type: `application/json`
- Body fields:
	- `orderId` (string)
	- `productName` (string)
	- `quantity` (number)
	- `price` (number)

Example body:

```json
{
	"orderId": "ORD-001",
	"productName": "Laptop",
	"quantity": 2,
	"price": 1200.50
}
```

## Run Tests

Run tests inside each service module:

```powershell
cd .\api-gateway; .\mvnw test
cd ..\order-service; .\mvnw test
cd ..\inventory-service; .\mvnw test
cd ..\billing-service; .\mvnw test
```

## Notes

- `start-all-services.ps1` and `start-all-services.bat` currently exist but are empty.
- If requests fail, verify:
	- Kafka is running on `localhost:9092`
	- all 4 services are up on their configured ports
