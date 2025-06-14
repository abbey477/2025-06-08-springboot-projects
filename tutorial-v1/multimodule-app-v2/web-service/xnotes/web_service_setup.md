# Web Service Setup

## Directory Structure
```
web-service/
├── pom.xml
└── src/main/
    ├── java/com/example/webservice/
    │   ├── WebServiceApplication.java
    │   ├── client/
    │   │   └── UserServiceClient.java
    │   └── controller/
    │       └── WebController.java
    └── resources/
        └── application.properties
```

## pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>web-service</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>24</maven.compiler.source>
        <maven.compiler.target>24</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Shared Common Library -->
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>common-lib</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
        <!-- Spring Boot dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## WebServiceApplication.java
```java
package com.example.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.webservice", "com.example.common"})
public class WebServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }
}
```

## UserServiceClient.java
```java
package com.example.webservice.client;

import com.example.common.model.User;
import com.example.common.model.ApiResponse;
import com.example.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;
    
    public Optional<ApiResponse> getAllUsers() {
        try {
            ResponseEntity<ApiResponse> response = restTemplate.getForEntity(
                userServiceUrl + "/api/users", 
                ApiResponse.class
            );
            log.info("Got response from user service: {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error calling user service: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    public Optional<ApiResponse> createUser(User user) {
        try {
            // Use shared utilities for preprocessing
            if (StringUtils.isNotEmpty(user.getName())) {
                user.setName(StringUtils.sanitize(user.getName()));
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> request = new HttpEntity<>(user, headers);
            
            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                userServiceUrl + "/api/users",
                request,
                ApiResponse.class
            );
            log.info("Created user via user service: {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    public Optional<ApiResponse> getUserById(Long id) {
        try {
            ResponseEntity<ApiResponse> response = restTemplate.getForEntity(
                userServiceUrl + "/api/users/" + id,
                ApiResponse.class
            );
            log.info("Got user by id from user service: {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error getting user by id: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    public Optional<ApiResponse> updateUser(Long id, User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> request = new HttpEntity<>(user, headers);
            
            restTemplate.put(userServiceUrl + "/api/users/" + id, request);
            
            // Get updated user
            return getUserById(id);
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    public boolean deleteUser(Long id) {
        try {
            restTemplate.delete(userServiceUrl + "/api/users/" + id);
            log.info("Deleted user with id: {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return false;
        }
    }
    
    public Optional<ApiResponse> checkUserServiceHealth() {
        try {
            ResponseEntity<ApiResponse> response = restTemplate.getForEntity(
                userServiceUrl + "/actuator/health",
                ApiResponse.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("User service health check failed: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
```

## WebController.java
```java
package com.example.webservice.controller;

import com.example.common.model.User;
import com.example.common.model.ApiResponse;
import com.example.common.util.ValidationUtils;
import com.example.webservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
public class WebController {
    
    private final UserServiceClient userServiceClient;
    
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUsers() {
        log.info("Web layer: Getting all users");
        
        return userServiceClient.getAllUsers()
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        log.info("Web layer: Getting user by id: {}", id);
        
        return userServiceClient.getUserById(id)
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }
    
    @PostMapping("/users")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
        log.info("Web layer: Creating user with email: {}", user.getEmail());
        
        // Use shared validation from common library
        if (!ValidationUtils.isValidName(user.getName())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid name provided"));
        }
        
        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email provided"));
        }
        
        return userServiceClient.createUser(user)
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Web layer: Updating user with id: {}", id);
        
        // Basic validation
        if (user.getName() != null && !ValidationUtils.isValidName(user.getName())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid name provided"));
        }
        
        if (user.getEmail() != null && !ValidationUtils.isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email provided"));
        }
        
        return userServiceClient.updateUser(id, user)
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        log.info("Web layer: Deleting user with id: {}", id);
        
        boolean deleted = userServiceClient.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } else {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to delete user"));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        log.info("Web layer: Health check");
        return ResponseEntity.ok(ApiResponse.success("Web service is running!"));
    }
    
    @GetMapping("/validate-email/{email}")
    public ResponseEntity<ApiResponse> validateEmail(@PathVariable String email) {
        log.info("Web layer: Validating email: {}", email);
        
        boolean isValid = ValidationUtils.isValidEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Email validation result", isValid));
    }
    
    @GetMapping("/validate-name/{name}")
    public ResponseEntity<ApiResponse> validateName(@PathVariable String name) {
        log.info("Web layer: Validating name: {}", name);
        
        boolean isValid = ValidationUtils.isValidName(name);
        return ResponseEntity.ok(ApiResponse.success("Name validation result", isValid));
    }
    
    @GetMapping("/user-service-status")
    public ResponseEntity<ApiResponse> checkUserServiceStatus() {
        log.info("Web layer: Checking user service status");
        
        return userServiceClient.checkUserServiceHealth()
                .map(response -> ResponseEntity.ok(ApiResponse.success("User service is healthy", response)))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service is down")));
    }
}
```

## application.properties
```properties
server.port=8082
spring.application.name=web-service

# External service URLs
user.service.url=http://localhost:8081

# Actuator
management.endpoints.web.exposure.include=health,info

# Logging
logging.level.com.example=DEBUG
logging.level.org.springframework.web.client=DEBUG
```

## Build and Run Commands
```bash
# Create directory structure
mkdir -p web-service/src/main/java/com/example/webservice/{client,controller}
mkdir -p web-service/src/main/resources

# Copy all files above to their respective locations

# Build (make sure common-lib is installed first)
cd web-service
mvn clean install

# Run the service
mvn spring-boot:run

# Or run JAR
java -jar target/web-service-1.0.0-SNAPSHOT.jar
```

## Test Commands

### Direct Web Service Tests
```bash
# Health check
curl http://localhost:8082/actuator/health

# Web service health
curl http://localhost:8082/api/web/health

# Check user service status
curl http://localhost:8082/api/web/user-service-status
```

### User Operations via Web Service
```bash
# Get all users (calls user service)
curl http://localhost:8082/api/web/users

# Create user via web service
curl -X POST http://localhost:8082/api/web/users \
  -H "Content-Type: application/json" \
  -d '{"name":"web user","email":"web@example.com"}'

# Get user by ID
curl http://localhost:8082/api/web/users/1

# Update user
curl -X PUT http://localhost:8082/api/web/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"updated web user","email":"updated@example.com"}'

# Delete user
curl -X DELETE http://localhost:8082/api/web/users/3
```

### Validation Endpoints (Using Shared Utils)
```bash
# Validate email
curl http://localhost:8082/api/web/validate-email/test@example.com
curl http://localhost:8082/api/web/validate-email/invalid-email

# Validate name
curl http://localhost:8082/api/web/validate-name/John
curl http://localhost:8082/api/web/validate-name/A
```

## Testing Invalid Data
```bash
# Test invalid email
curl -X POST http://localhost:8082/api/web/users \
  -H "Content-Type: application/json" \
  -d '{"name":"test","email":"invalid-email"}'

# Test invalid name (too short)
curl -X POST http://localhost:8082/api/web/users \
  -H "Content-Type: application/json" \
  -d '{"name":"A","email":"test@example.com"}'

# Test empty name
curl -X POST http://localhost:8082/api/web/users \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"test@example.com"}'
```

## Expected Responses

### Successful User Creation
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 3,
    "name": "Web User",
    "email": "web@example.com"
  }
}
```

### Validation Error
```json
{
  "success": false,
  "message": "Invalid email provided",
  "data": null
}
```

### Service Unavailable
```json
{
  "success": false,
  "message": "User service unavailable",
  "data": null
}
```

## Prerequisites
1. **Java 24** must be installed
2. **Maven** must be installed  
3. **common-lib must be built and installed** first:
   ```bash
   cd common-lib && mvn clean install
   ```
4. **user-service must be running** on port 8081:
   ```bash
   cd user-service && mvn spring-boot:run
   ```

## Service Dependencies
- **Web Service** (Port 8082) → calls → **User Service** (Port 8081)
- Both services use **common-lib** for shared models and utilities
- Web service validates data using shared validation utilities
- Web service preprocesses data using shared string utilities

## Architecture Flow
```
HTTP Request → Web Service (8082) → User Service (8081) → Database
                     ↓                        ↓
               Shared Validation      Shared Models & Utils
               Shared String Utils    
```

The Web Service will run on **http://localhost:8082** and proxy requests to the User Service running on **http://localhost:8081**.