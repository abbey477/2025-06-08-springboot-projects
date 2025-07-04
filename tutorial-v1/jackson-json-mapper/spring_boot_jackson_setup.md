# Spring Boot Jackson Setup Guide

Complete guide for setting up Jackson with Spring Boot using the latest libraries and configurations.

## Maven Dependencies

### Core Spring Boot with Jackson (Latest Versions)

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
        <version>3.3.1</version> <!-- Latest Spring Boot 3.x -->
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>jackson-demo</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <java.version>17</java.version>
        <jackson.version>2.17.1</jackson.version> <!-- Latest Jackson -->
    </properties>
    
    <dependencies>
        <!-- Spring Boot Web Starter (includes Jackson by default) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Spring Boot Validation Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- Jackson Core Dependencies (usually included in spring-boot-starter-web) -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson Java Time Module (for LocalDate, LocalDateTime, etc.) -->
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson Parameter Names Module -->
        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-parameter-names</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson Kotlin Module (if using Kotlin) -->
        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-kotlin</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson XML Support -->
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-xml</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson YAML Support -->
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-yaml</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson CSV Support -->
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-csv</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson JSON Schema Generator -->
        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-jsonSchema</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Jackson Hibernate Module (for JPA entities) -->
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-hibernate5</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        
        <!-- Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

---

## Spring Boot Configuration

### Application Properties Configuration

```properties
# application.properties

# Jackson Configuration
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.serialization.write-date-timestamps-as-nanoseconds=false
spring.jackson.serialization.fail-on-empty-beans=false
spring.jackson.serialization.indent-output=true

# Deserialization Configuration
spring.jackson.deserialization.fail-on-unknown-properties=false
spring.jackson.deserialization.fail-on-null-for-primitives=false
spring.jackson.deserialization.accept-single-value-as-array=true

# Property Naming Strategy
spring.jackson.property-naming-strategy=SNAKE_CASE

# Date Format
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=UTC

# Default Property Inclusion
spring.jackson.default-property-inclusion=NON_NULL

# Locale
spring.jackson.locale=en_US

# Parser Features
spring.jackson.parser.allow-comments=true
spring.jackson.parser.allow-yaml-comments=true
spring.jackson.parser.allow-unquoted-field-names=true
spring.jackson.parser.allow-single-quotes=true

# Generator Features
spring.jackson.generator.write-numbers-as-strings=false
spring.jackson.generator.quote-field-names=true
```

### YAML Configuration Alternative

```yaml
# application.yml
spring:
  jackson:
    serialization:
      write-dates-as-timestamps: false
      write-date-timestamps-as-nanoseconds: false
      fail-on-empty-beans: false
      indent-output: true
    deserialization:
      fail-on-unknown-properties: false
      fail-on-null-for-primitives: false
      accept-single-value-as-array: true
    property-naming-strategy: SNAKE_CASE
    date-format: "yyyy-MM-dd HH:mm:ss"
    time-zone: UTC
    default-property-inclusion: NON_NULL
    locale: en_US
    parser:
      allow-comments: true
      allow-yaml-comments: true
      allow-unquoted-field-names: true
      allow-single-quotes: true
    generator:
      write-numbers-as-strings: false
      quote-field-names: true
```

---

## Java Configuration Classes

### Jackson Configuration Bean

```java
package com.example.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                // Modules
                .modules(new JavaTimeModule(), new ParameterNamesModule())
                
                // Serialization features
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
                
                // Deserialization features
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                
                // Property inclusion
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                
                // Naming strategy
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                
                // Date format
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .timeZone(TimeZone.getTimeZone("UTC"))
                
                .build();
    }

    @Bean
    public ObjectMapper xmlMapper() {
        return Jackson2ObjectMapperBuilder.xml()
                .modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }
}
```

### Custom Serializers and Deserializers Registration

```java
package com.example.config;

import com.example.serializers.CustomDateSerializer;
import com.example.serializers.CustomDateDeserializer;
import com.example.serializers.MoneySerializer;
import com.example.serializers.MoneyDeserializer;
import com.example.model.Money;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class CustomSerializersConfig {

    @Bean
    public SimpleModule customSerializersModule() {
        SimpleModule module = new SimpleModule("CustomSerializers");
        
        // Register custom serializers
        module.addSerializer(Date.class, new CustomDateSerializer());
        module.addSerializer(Money.class, new MoneySerializer());
        
        // Register custom deserializers
        module.addDeserializer(Date.class, new CustomDateDeserializer());
        module.addDeserializer(Money.class, new MoneyDeserializer());
        
        return module;
    }
}
```

### Filter Configuration

```java
package com.example.config;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterProvider filterProvider() {
        SimpleFilterProvider filters = new SimpleFilterProvider();
        
        // Public user filter
        filters.addFilter("userFilter", 
            SimpleBeanPropertyFilter.filterOutAllExcept(
                "id", "username", "firstName", "lastName", "email"));
        
        // Admin user filter
        filters.addFilter("adminUserFilter", 
            SimpleBeanPropertyFilter.serializeAllExcept("password", "ssn"));
        
        // Product filter
        filters.addFilter("productFilter", 
            SimpleBeanPropertyFilter.serializeAll());
        
        return filters;
    }
}
```

---

## Spring Boot Main Application

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JacksonDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(JacksonDemoApplication.class, args);
    }
}
```

---

## Model Classes with Jackson Annotations

### User Entity with JPA and Jackson

```java
package com.example.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@JsonFilter("userFilter")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @Column(nullable = false)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Column(name = "is_active")
    private boolean active = true;

    // Constructors
    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Computed property
    @JsonGetter("full_name")
    public String getFullName() {
        if (firstName == null && lastName == null) return null;
        return (firstName != null ? firstName : "") + " " + 
               (lastName != null ? lastName : "");
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
```

### Order Entity

```java
package com.example.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @JsonFormat(pattern = "#,##0.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    // Constructors
    public Order() {}

    public Order(String orderNumber, LocalDateTime orderDate, BigDecimal totalAmount, OrderStatus status) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
```

---

## REST Controller with Jackson Features

```java
package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Public endpoint - filtered response
    @GetMapping("/public")
    public ResponseEntity<String> getPublicUsers() throws Exception {
        List<User> users = userService.findAll();
        
        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("userFilter", SimpleBeanPropertyFilter
                .filterOutAllExcept("user_id", "username", "full_name"));
        
        String json = objectMapper.writer(filters).writeValueAsString(users);
        return ResponseEntity.ok(json);
    }

    // Admin endpoint - full response
    @GetMapping("/admin")
    public ResponseEntity<String> getAdminUsers() throws Exception {
        List<User> users = userService.findAll();
        
        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("userFilter", SimpleBeanPropertyFilter.serializeAll());
        
        String json = objectMapper.writer(filters)
            .withDefaultPrettyPrinter()
            .writeValueAsString(users);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }
}
```

---

## Service Layer

```java
package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
```

---

## Testing Configuration

```java
package com.example.test;

import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("testuser", "test@email.com", "password");
        user.setFirstName("Test");
        user.setLastName("User");

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.full_name").value("Test User"));
    }
}
```

This comprehensive setup provides everything needed to work with Jackson annotations in Spring Boot, including the latest dependencies, configuration options, and practical examples of how to use them in a real application.