# User Service Setup

## Directory Structure
```
user-service/
├── pom.xml
└── src/main/
    ├── java/com/example/userservice/
    │   ├── UserServiceApplication.java
    │   ├── repository/
    │   │   └── UserRepository.java
    │   ├── service/
    │   │   └── UserService.java
    │   └── controller/
    │       └── UserController.java
    └── resources/
        ├── application.properties
        ├── schema.sql
        └── data.sql
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
    <artifactId>user-service</artifactId>
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
            <artifactId>spring-boot-starter-data-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
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

## UserServiceApplication.java
```java
package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.userservice", "com.example.common"})
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

## UserRepository.java
```java
package com.example.userservice.repository;

import com.example.common.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    @Query("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    
    @Query("SELECT * FROM users WHERE name LIKE %:name%")
    Iterable<User> findByNameContaining(@Param("name") String name);
}
```

## UserService.java
```java
package com.example.userservice.service;

import com.example.common.model.User;
import com.example.common.util.StringUtils;
import com.example.common.util.ValidationUtils;
import com.example.common.exception.BusinessException;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email);
    }
    
    public User createUser(User user) {
        // Use shared validation utilities
        if (!ValidationUtils.isValidName(user.getName())) {
            throw new BusinessException("Invalid name: " + user.getName());
        }
        
        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            throw new BusinessException("Invalid email: " + user.getEmail());
        }
        
        // Use shared string utilities
        user.setName(StringUtils.capitalize(StringUtils.sanitize(user.getName())));
        user.setEmail(StringUtils.sanitize(user.getEmail()));
        
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("User with email " + user.getEmail() + " already exists");
        }
        
        User savedUser = userRepository.save(user);
        log.info("Created user: {}", savedUser);
        return savedUser;
    }
    
    public User updateUser(Long id, User userUpdate) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (StringUtils.isNotEmpty(userUpdate.getName())) {
                        if (!ValidationUtils.isValidName(userUpdate.getName())) {
                            throw new BusinessException("Invalid name: " + userUpdate.getName());
                        }
                        existingUser.setName(StringUtils.capitalize(StringUtils.sanitize(userUpdate.getName())));
                    }
                    if (StringUtils.isNotEmpty(userUpdate.getEmail())) {
                        if (!ValidationUtils.isValidEmail(userUpdate.getEmail())) {
                            throw new BusinessException("Invalid email: " + userUpdate.getEmail());
                        }
                        existingUser.setEmail(StringUtils.sanitize(userUpdate.getEmail()));
                    }
                    User updated = userRepository.save(existingUser);
                    log.info("Updated user: {}", updated);
                    return updated;
                })
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }
}
```

## UserController.java
```java
package com.example.userservice.controller;

import com.example.common.model.User;
import com.example.common.model.ApiResponse;
import com.example.common.exception.BusinessException;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            log.error("Error getting users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve users"));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            return userService.getUserById(id)
                    .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User not found")));
        } catch (Exception e) {
            log.error("Error getting user by id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve user"));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", createdUser));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create user"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating user: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update user"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting user: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete user"));
        }
    }
}
```

## application.properties
```properties
server.port=8081
spring.application.name=user-service

# H2 Database
spring.datasource.url=jdbc:h2:mem:userdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Database Initialization
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql

# Actuator
management.endpoints.web.exposure.include=health,info,metrics

# Logging
logging.level.com.example=DEBUG
```

## schema.sql
```sql
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);
```

## data.sql
```sql
INSERT INTO users (name, email) 
SELECT * FROM (SELECT 'John Doe', 'john.doe@example.com') AS tmp
WHERE NOT EXISTS (SELECT email FROM users WHERE email = 'john.doe@example.com');

INSERT INTO users (name, email) 
SELECT * FROM (SELECT 'Jane Smith', 'jane.smith@example.com') AS tmp
WHERE NOT EXISTS (SELECT email FROM users WHERE email = 'jane.smith@example.com');
```

## Build and Run Commands
```bash
# Create directory structure
mkdir -p user-service/src/main/java/com/example/userservice/{repository,service,controller}
mkdir -p user-service/src/main/resources

# Copy all files above to their respective locations

# Build (make sure common-lib is installed first)
cd user-service
mvn clean install

# Run the service
mvn spring-boot:run

# Or run JAR
java -jar target/user-service-1.0.0-SNAPSHOT.jar
```

## Test Commands
```bash
# Health check
curl http://localhost:8081/actuator/health

# Get all users
curl http://localhost:8081/api/users

# Create user
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"test user","email":"test@example.com"}'

# Get user by ID
curl http://localhost:8081/api/users/1

# Update user
curl -X PUT http://localhost:8081/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"updated user","email":"updated@example.com"}'

# Delete user
curl -X DELETE http://localhost:8081/api/users/3

# H2 Console: http://localhost:8081/h2-console
# JDBC URL: jdbc:h2:mem:userdb
# Username: sa
# Password: (empty)
```

## Prerequisites
1. **Java 24** must be installed
2. **Maven** must be installed  
3. **common-lib must be built and installed** first:
   ```bash
   cd common-lib && mvn clean install
   ```

The User Service will run on **http://localhost:8081**