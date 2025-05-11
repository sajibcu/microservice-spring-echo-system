# Microservice Architecture Project

## Overview
This project implements a microservice architecture with Spring Boot, featuring:
- API Gateway for centralized routing
- Service Discovery with Eureka Server
- User Service for user management
- Product Service for product catalog
- Sales Service for order management
- PostgreSQL databases for each service
- ELK Stack for centralized logging
- Docker containerization

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
            │              │              │
            └──────────────┼──────────────┘
                          ▼
                    [ELK Stack]
```

## Services & Ports

| Service | Port | Database Port | Description |
|---------|------|--------------|-------------|
| API Gateway | 1001 | - | Spring Cloud Gateway |
| Eureka Server | 8761 | - | Service Discovery |
| User Service | 8081 | 6601 | User Management |
| Product Service | 8082 | 6602 | Product Catalog |
| Sales Service | 8083 | 6603 | Order Management |
| Elasticsearch | 9200, 9300 | - | Search & Analytics |
| Logstash | 5000, 5044, 9600 | - | Log Ingestion |
| Kibana | 5601 | - | Log Visualization |

## Prerequisites
- Docker and Docker Compose
- Java 17 or higher
- Gradle
- Git
- 8GB RAM minimum

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
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:1001
- User Service: http://localhost:8081
- Product Service: http://localhost:8082
- Sales Service: http://localhost:8083
- Kibana: http://localhost:5601

## API Documentation

### User Service
```
GET    /api/v1/user         # List all users
POST   /api/v1/user         # Create new user
GET    /api/v1/user/{id}    # Get user by ID
PUT    /api/v1/user/{id}    # Update user
DELETE /api/v1/user/{id}    # Delete user
```

### Product Service
```
GET    /api/v1/product         # List all products
POST   /api/v1/product         # Create new product
GET    /api/v1/product/{id}    # Get product by ID
PUT    /api/v1/product/{id}    # Update product
DELETE /api/v1/product/{id}    # Delete product
```

### Sales Service
```
GET    /api/v1/sales         # List all sales
POST   /api/v1/sales         # Create new sale
GET    /api/v1/sales/{id}    # Get sale by ID
PUT    /api/v1/sales/{id}    # Update sale
DELETE /api/v1/sales/{id}    # Delete sale
```

## Development

### Building Individual Services
```bash
cd <service-directory>
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Logging
- Access Kibana at http://localhost:5601
- View service logs: `docker-compose logs -f [service-name]`
- ELK Stack dashboard available in Kibana

## Troubleshooting

1. Check service health:
```bash
docker-compose ps
```

2. View service logs:
```bash
docker-compose logs -f [service-name]
```

3. Restart specific service:
```bash
docker-compose restart [service-name]
```

4. Common issues:
- Port conflicts: Ensure ports 1001, 8761, 8081-8083 are available
- Memory issues: Increase Docker memory limit
- Connection refused: Check if Eureka Server is running

## Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/name`)
3. Commit changes (`git commit -am 'Add feature'`)
4. Push branch (`git push origin feature/name`)
5. Create Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
