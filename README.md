# Microservice Architecture Project

## Overview
This project implements a microservice architecture with the following components:
- API Gateway
- Service Discovery (Eureka Server)
- User Service
- Product Service
- Sales Service
- PostgreSQL databases for each service

## Architecture
```
                           │
                           ▼
                    [API Gateway:1001]
                           │
                    [Eureka Server]
                           │
            ┌──────────────┼──────────────┐
            ▼              ▼              ▼
    [User Service]  [Product Service] [Sales Service]
            │              │              │
            ▼              ▼              ▼
    [UserService DB] [ProductService DB] [SalesService DB]
```

## Services & Ports

| Service | Port | Database Port |
|---------|------|--------------|
| API Gateway | 1001 | - |
| Eureka Server | 8761 | - |
| User Service | 8081 | 6601 |
| Product Service | 8082 | 6602 |
| Sales Service | 8083 | 6603 |

## Prerequisites
- Docker and Docker Compose
- Java 17 or higher
- Gradle

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/sajibcu/microservice-spring-echo-system.git
cd microservice
```

2. Build and run the services:
```bash
docker-compose up -d --build
```

3. Verify the services:
- Eureka Dashboard: http://localhost:9000
- API Gateway: http://localhost:1001
- User Service: http://localhost:8081
- Product Service: http://localhost:8082
- Sales Service: http://localhost:8083
