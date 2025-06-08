# Multi-Module Project Structure

```
multimodule-app/                    # Root directory
├── pom.xml                        # Parent POM (from first artifact)
├── service-module/                # Service module directory
│   ├── pom.xml                   # Service module POM
│   └── src/
│       └── main/
│           └── java/
│               └── com/
│                   └── example/
│                       └── service/
├── web-module/                    # Web module directory
│   ├── pom.xml                   # Web module POM
│   └── src/
│       └── main/
│           └── java/
│               └── com/
│                   └── example/
│                       └── web/
└── README.md
```

## Key Features

### Shared Configuration (Parent POM)
- **Spring Boot 3.5.0** with Java 21
- **Lombok 1.18.30** for reducing boilerplate code
- **Dependency Management** for consistent versions across modules
- **Shared plugins**: Maven Compiler (with Lombok annotation processing), Surefire, Spring Boot
- **Common dependencies**: Spring Boot Starter, Testing framework, Lombok

### Service Module
- **Business logic layer**
- Spring Data JPA for data persistence
- H2 database for development/testing
- Bean validation support
- No executable JAR (library module)

### Web Module  
- **Presentation layer** with REST controllers
- Depends on service-module
- Spring Web for REST APIs
- Spring Actuator for monitoring
- **Executable JAR** with Spring Boot plugin

## Build Commands

```bash
# Build all modules
mvn clean install

# Build specific module
mvn clean install -pl web-module

# Run the web application
cd web-module
mvn spring-boot:run
```

## Benefits

1. **Shared Dependencies**: Common libraries defined once in parent
2. **Plugin Management**: Consistent build configuration
3. **Version Control**: Single source of truth for versions
4. **Modular Design**: Clear separation of concerns
5. **Reusability**: Service module can be used by multiple web modules
6. **Lombok Integration**: Automatic annotation processing across all modules
7. **Reduced Boilerplate**: Clean, readable code with @Data, @Builder, @RequiredArgsConstructor