# Common Library Setup

## Directory Structure
```
common-lib/
├── pom.xml
└── src/main/java/com/example/common/
    ├── model/
    │   ├── User.java
    │   └── ApiResponse.java
    ├── util/
    │   ├── StringUtils.java
    │   └── ValidationUtils.java
    ├── config/
    │   └── CommonConfig.java
    └── exception/
        └── BusinessException.java
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
    <artifactId>common-lib</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>24</maven.compiler.source>
        <maven.compiler.target>24</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- NO spring-boot-maven-plugin - this is a library -->
</project>
```

## User.java
```java
package com.example.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}
```

## ApiResponse.java
```java
package com.example.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
    
    public static ApiResponse success(Object data) {
        return new ApiResponse(true, "Success", data);
    }
    
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }
    
    public static ApiResponse error(String message) {
        return new ApiResponse(false, message, null);
    }
    
    public static ApiResponse success(String message) {
        return new ApiResponse(true, message, null);
    }
}
```

## StringUtils.java
```java
package com.example.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
    
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static String sanitize(String str) {
        if (str == null) {
            return null;
        }
        return str.trim().replaceAll("[<>\"'&]", "");
    }
}
```

## ValidationUtils.java
```java
package com.example.common.util;

import lombok.experimental.UtilityClass;
import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidName(String name) {
        return StringUtils.isNotEmpty(name) && name.length() >= 2 && name.length() <= 50;
    }
}
```

## CommonConfig.java
```java
package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

## BusinessException.java
```java
package com.example.common.exception;

public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

## Build Commands
```bash
# Create the directory structure
mkdir -p common-lib/src/main/java/com/example/common/{model,util,config,exception}

# Copy all the files above to their respective locations

# Build and install to local Maven repository
cd common-lib
mvn clean install
```

## Test Build
```bash
# Verify it builds successfully
mvn clean compile

# Check if JAR is created
ls target/

# Install to local repo (other projects can use it)
mvn install
```

## Notes
- **No main application class** - this is a shared library
- **No spring-boot-maven-plugin** - creates regular JAR, not executable
- **Install to local Maven repo** - other services can depend on it
- **Version 1.0.0-SNAPSHOT** - can be updated as needed

Once this builds successfully, you can use it in other microservices by adding this dependency:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```