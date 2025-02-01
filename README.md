# E-Commerce Application

A basic e-commerce platform built with JAX-RS (Jersey), Vue.js, and PostgreSQL.

## Prerequisites

- JDK 23
- Node.js 18+ and npm
- Gradle 8.5+
- Docker and Docker Compose

## Setup Instructions

### 1. Docker Environment Setup
1. Install Docker from [Docker's Official Website](https://www.docker.com/products/docker-desktop/)
2. Start Docker Desktop
3. Clone the repository and navigate to project directory
4. Start the infrastructure services:
```bash
docker-compose up -d
```

This will start:
- PostgreSQL (accessible on port 5433)
- Kafka (port 9092)
- Zookeeper (port 2181)
- Kafka UI (port 8080)

### 2. IntelliJ IDEA Setup
- Open IntelliJ
- File → Open → Select project folder
- Wait for Gradle sync to complete

### 3. Application Configuration

Create `application.properties` in `src/main/resources/`:
```properties
spring.application.name={microservice_name}
server.port=8088

spring.jpa.show-sql=true

spring.jpa.database=postgresql
spring.datasource.url=jdbc:postgresql://localhost:5433/test_database
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update

logging.level.org.springframework.cache=TRACE

spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=2000

kafka.bootstrap.servers=localhost:9092
kafka.client.id={service-name}
kafka.consumer.group={service-name}-group
```

### 4. Running the Backend
1. Ensure Docker containers are running:
```bash
docker ps
```

### 5. Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

## Docker Commands

Common Docker commands for development:

```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# Access PostgreSQL CLI
docker exec -it postgres psql -U root -d test_database

# Restart a specific service
docker-compose restart postgres
```

## API Endpoints

### User Service (Port: 8080)
Handles user authentication and management
```http
POST /api/v1/user/register
POST /api/v1/user/login
GET  /api/v1/user/profile
PUT  /api/v1/user/profile
```

### Product Service (Port: 8081)
Manages product catalog and inventory
```http
GET    /api/v1/product
POST   /api/v1/product
PUT    /api/v1/product/{id}
DELETE /api/v1/product/{id}
```

### Order Service (Port: 8082)
Handles order processing and management
```http
GET    /api/v1/order
POST   /api/v1/order
GET    /api/v1/order/{id}
GET    /api/v1/order/self
```

### Stats Service (Port: 8083)
Provides analytics and statistics
```http
GET /api/v1/stats/dashboard?interval=week
GET /api/v1/stats/sales?interval=week
GET /api/v1/stats/products?interval=week
GET /api/v1/stats/users?interval=week
```

## Kafka events

Order and products services:

- **Order Completed Event**
  - Producer: Order Service
  - Consumers: Stats Service (analytics)
  - Topic: `order.completed`

Order and stats services

- **Order Completed Event**
  - Producer: Order Service
  - Consumers: Stats Service (analytics)
  - Topic: `order.completed`

Kafka UI is available at http://localhost:8080 for monitoring events and topics.