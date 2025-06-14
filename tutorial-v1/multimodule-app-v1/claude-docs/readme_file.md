# Multi-Module Spring Boot Application

A simple multi-module Spring Boot 3.5.0 application demonstrating clean architecture with Maven, Lombok, and RESTful APIs.

## 🏗️ Project Structure

```
multimodule-app/
├── pom.xml                           # Parent POM
├── service-module/                   # Business Logic Layer
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/example/service/
│       │   ├── config/
│       │   │   └── ServiceConfig.java
│       │   ├── dto/
│       │   │   └── UserDto.java
│       │   ├── model/
│       │   │   └── User.java
│       │   ├── repository/
│       │   │   └── UserRepository.java
│       │   └── UserService.java
│       └── test/java/com/example/service/
│           └── UserServiceTest.java
├── web-module/                       # Presentation Layer
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/example/web/
│       │   │   ├── config/
│       │   │   │   └── DataInitializer.java
│       │   │   ├── controller/
│       │   │   │   └── UserController.java
│       │   │   ├── exception/
│       │   │   │   └── GlobalExceptionHandler.java
│       │   │   └── WebApplication.java
│       │   └── resources/
│       │       ├── application.properties
│       │       └── schema.sql
│       └── test/java/com/example/web/
│           └── controller/
│               └── UserControllerTest.java
└── README.md
```

## 🚀 Features

### Technology Stack
- **Spring Boot 3.5.0** with Java 21
- **Lombok 1.18.30** for reducing boilerplate code
- **Spring Data JDBC** for simple data persistence
- **H2 Database** for development and testing
- **Maven** for dependency management
- **JUnit 5** and **Mockito** for testing

### Architecture
- **Multi-module design** with clear separation of concerns
- **Service Layer** for business logic
- **Web Layer** for REST API endpoints
- **Repository Pattern** with Spring Data JDBC
- **DTO Pattern** for data transfer
- **Global Exception Handling**

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| GET | `/api/users/department/{dept}` | Get users by department |
| GET | `/api/users/search?name={name}` | Search users by name |

### Additional Endpoints
- **H2 Console**: `http://localhost:8080/h2-console`
- **Health Check**: `http://localhost:8080/actuator/health`
- **Metrics**: `http://localhost:8080/actuator/metrics`

## 🛠️ Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+ 

### Building the Project

1. **Clone and navigate to the project directory**
```bash
git clone <repository-url>
cd multimodule-app
```

2. **Build all modules**
```bash
mvn clean install
```

3. **Run the application**
```bash
cd web-module
mvn spring-boot:run
```

4. **Access the application**
   - API Base URL: `http://localhost:8080/api/users`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: (empty)

### Sample API Usage

**Create a new user:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "department": "IT"
  }'
```

**Get all users:**
```bash
curl http://localhost:8080/api/users
```

**Search users by name:**
```bash
curl "http://localhost:8080/api/users/search?name=John"
```

## 🧪 Testing

### Run all tests
```bash
mvn test
```

### Run tests for specific module
```bash
mvn test -pl service-module
mvn test -pl web-module
```

### Test Coverage
- **Service Layer**: Unit tests with Mockito
- **Web Layer**: Integration tests with MockMvc
- **Repository Layer**: Tested through service layer tests

## 📊 Sample Data

The application automatically initializes with sample users:
- John Doe (IT Department)
- Jane Smith (HR Department)  
- Bob Johnson (IT Department)
- Alice Brown (Finance Department)

## 🔧 Configuration

### Database Configuration
- **Development**: H2 in-memory database
- **Production**: Configure datasource in `application.properties`

### Logging
- Application logs: `DEBUG` level for `com.example`
- SQL logs: `DEBUG` level for Hibernate
- Parameter binding: `TRACE` level

### Validation
- Bean validation with `@Valid` annotations
- Custom error responses for validation failures
- Email format validation
- Required field validation

## 🏆 Benefits

1. **Modular Architecture**: Clear separation between business logic and presentation
2. **Shared Dependencies**: Centralized dependency management in parent POM
3. **Clean Code**: Lombok reduces boilerplate significantly
4. **Comprehensive Testing**: Unit and integration tests included
5. **Production Ready**: Exception handling, validation, and logging configured
6. **Easy Development**: H2 console for database inspection
7. **Monitoring**: Spring Actuator endpoints for health checks

## 🚀 Next Steps

To extend this project, consider adding:
- **Security**: Spring Security for authentication/authorization
- **Documentation**: OpenAPI/Swagger for API documentation
- **Caching**: Redis or Caffeine for performance
- **Database**: PostgreSQL or MySQL for production
- **Monitoring**: Micrometer metrics and distributed tracing
- **Validation**: Custom validators and more complex business rules