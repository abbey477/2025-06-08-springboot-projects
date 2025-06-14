# Complete Web Module Project Structure

## 📁 Web Module Directory Structure

```
web-module/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── web/
│   │   │               ├── config/
│   │   │               │   └── DataInitializer.java
│   │   │               ├── controller/
│   │   │               │   └── UserController.java
│   │   │               ├── exception/
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               └── WebApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── web/
│                       └── controller/
│                           └── UserControllerTest.java
```

## 📄 All Web Module Files

### 1. pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- Parent Reference -->
    <parent>
        <groupId>com.example</groupId>
        <artifactId>multimodule-app</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>web-module</artifactId>
    <packaging>jar</packaging>

    <name>Web Module</name>
    <description>Web layer with REST controllers</description>

    <!-- Module-specific dependencies -->
    <dependencies>
        <!-- Dependency on service module -->
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>service-module</artifactId>
        </dependency>

        <!-- Spring Boot Web Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Actuator -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Spring Boot Maven Plugin for executable JAR -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. WebApplication.java (Main Class)
```java
package com.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.web", "com.example.service"})
public class WebApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
```

### 3. UserController.java
```java
package com.example.web.controller;

import com.example.service.UserService;
import com.example.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("GET /api/users - Fetching all users");
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Fetching user by id", id);
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST /api/users - Creating new user: {}", userDto.getName());
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @GetMapping("/department/{department}")
    public ResponseEntity<List<UserDto>> getUsersByDepartment(@PathVariable String department) {
        log.info("GET /api/users/department/{} - Fetching users by department", department);
        List<UserDto> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsersByName(@RequestParam String name) {
        log.info("GET /api/users/search?name={} - Searching users by name", name);
        List<UserDto> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
}
```

### 4. GlobalExceptionHandler.java
```java
package com.example.web.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Input validation failed")
                .validationErrors(validationErrors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    @Data
    @lombok.Builder
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private Map<String, String> validationErrors;
    }
}
```

### 5. DataInitializer.java
```java
package com.example.web.config;

import com.example.service.UserService;
import com.example.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserService userService;
    
    @Override
    public void run(String... args) {
        log.info("Initializing sample data...");
        
        try {
            // Create sample users
            UserDto user1 = UserDto.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .department("IT")
                    .build();
            
            UserDto user2 = UserDto.builder()
                    .name("Jane Smith")
                    .email("jane.smith@example.com")
                    .department("HR")
                    .build();
            
            UserDto user3 = UserDto.builder()
                    .name("Bob Johnson")
                    .email("bob.johnson@example.com")
                    .department("IT")
                    .build();
            
            UserDto user4 = UserDto.builder()
                    .name("Alice Brown")
                    .email("alice.brown@example.com")
                    .department("Finance")
                    .build();
            
            userService.createUser(user1);
            userService.createUser(user2);
            userService.createUser(user3);
            userService.createUser(user4);
            
            log.info("Sample data initialized successfully!");
            
        } catch (Exception e) {
            log.error("Error initializing sample data: {}", e.getMessage());
        }
    }
}
```

### 6. application.properties
```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Application Configuration
spring.application.name=multimodule-app

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Spring Data JDBC Configuration
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql

# Jackson Configuration
spring.jackson.default-property-inclusion=non_null
spring.jackson.serialization.write-dates-as-timestamps=false

# Management Endpoints
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized

# Logging Configuration
logging.level.com.example=DEBUG
logging.level.org.springframework.jdbc=DEBUG
```

### 7. schema.sql
```sql
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(255)
);
```

### 8. UserControllerTest.java
```java
package com.example.web.controller;

import com.example.service.UserService;
import com.example.service.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .build();
        
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDto));
        
        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }
    
    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .build();
        
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(userDto));
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
    
    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        when(userService.getUserById(anyLong())).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createUser_WithValidData_ShouldCreateUser() throws Exception {
        // Given
        UserDto inputDto = UserDto.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .department("HR")
                .build();
        
        UserDto responseDto = UserDto.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .department("HR")
                .build();
        
        when(userService.createUser(any(UserDto.class))).thenReturn(responseDto);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }
}
```

## 🚀 Quick Setup Commands

```bash
# Create web-module directory structure
mkdir -p web-module/src/main/java/com/example/web/{config,controller,exception}
mkdir -p web-module/src/main/resources
mkdir -p web-module/src/test/java/com/example/web/controller

# Copy all files to their respective locations
# (Use the content above for each file)

# Build and run
cd web-module
mvn clean install
mvn spring-boot:run
```

## ✅ Features Included

- **REST API endpoints** for user management
- **Global exception handling** with custom error responses
- **Bean validation** with detailed error messages
- **Sample data initialization** on startup
- **Comprehensive testing** with MockMvc
- **Spring Actuator** for monitoring
- **CORS configuration** for frontend integration
- **Structured logging** with request/response details

## 🎯 API Endpoints Available

- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `GET /api/users/department/{dept}` - Filter by department
- `GET /api/users/search?name={name}` - Search by name
- `GET /actuator/health` - Health check
- `GET /h2-console` - Database console

Your web module is now complete and ready to run! 🎉