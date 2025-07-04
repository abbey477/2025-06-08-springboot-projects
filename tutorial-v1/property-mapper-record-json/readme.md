# Person Management Spring Boot Application

A Spring Boot 3.5.0 application demonstrating best practices with record classes, builder pattern, JSON marshalling/unmarshalling, validation, and comprehensive testing.

## Features

- **Person Record Class**: Immutable data class with validation annotations
- **Builder Pattern**: Easy creation and modification of Person objects
- **JSON Marshalling/Unmarshalling**: Service for converting between Person objects and JSON
- **Spring Boot Validation**: Input validation using Jakarta Bean Validation
- **Lombok Integration**: Clean logging without System.out.println
- **Comprehensive Unit Tests**: Full test coverage for all functionality
- **Standalone Application**: No web dependencies, pure Spring Boot application

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/personapp/
│   │       ├── PersonAppApplication.java      # Main Spring Boot application
│   │       ├── PersonDemoRunner.java          # Demo command line runner
│   │       ├── model/
│   │       │   └── Person.java                # Record class with builder pattern
│   │       └── service/
│   │           └── PersonService.java         # JSON marshalling service
│   └── resources/
│       └── application.properties             # Application configuration
└── test/
    └── java/
        └── com/example/personapp/
            ├── model/
            │   └── PersonValidationTest.java  # Validation tests
            └── service/
                └── PersonServiceTest.java     # Service tests
```

## Key Components

### Person Record Class

The `Person` record includes:
- **Fields**: name, email, dob (LocalDate), timestamp (LocalDateTime)
- **Validation**: @NotBlank, @Email, @NotNull, @Past annotations
- **Builder Pattern**: Static builder() method and toBuilder() for modifications
- **JSON Serialization**: Proper date/time formatting annotations

### PersonService

Provides methods for:
- Marshalling single Person objects to JSON
- Unmarshalling JSON to Person objects
- Marshalling/unmarshalling lists of Person objects
- Updating timestamps on Person objects
- Comprehensive error handling and logging

### Validation

Uses Jakarta Bean Validation with custom error messages:
- Name: Cannot be blank
- Email: Must be valid email format
- Date of birth: Cannot be null, must be in the past
- Timestamp: Cannot be null

## Dependencies

- Spring Boot 3.5.0
- Spring Boot Starter Web
- Spring Boot Starter Validation
- Jackson for JSON processing
- Lombok for logging
- JUnit 5 for testing

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build and Run
```bash
# Build the project
mvn clean compile

# Run tests
mvn test

# Run the application
mvn spring-boot:run
```

### Expected Output
The application includes a demo runner that demonstrates all functionality:
- Person creation using builder pattern
- JSON marshalling and unmarshalling
- Builder pattern usage with toBuilder()
- Timestamp updates
- List operations

## Usage Examples

### Creating a Person
```java
Person person = Person.builder()
    .name("John Doe")
    .email("john.doe@example.com")
    .dob(LocalDate.of(1990, 5, 15))
    .timestamp(LocalDateTime.now())
    .build();
```

### Modifying a Person
```java
Person modifiedPerson = person.toBuilder()
    .name("John Smith")
    .email("john.smith@example.com")
    .build();
```

### JSON Operations
```java
// Marshal to JSON
String json = personService.marshalToJson(person);

// Unmarshal from JSON
Person person = personService.unmarshalFromJson(json);
```

### Validation
```java
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();
Set<ConstraintViolation<Person>> violations = validator.validate(person);
```

## Testing

The project includes comprehensive unit tests:

- **PersonServiceTest**: Tests all JSON marshalling/unmarshalling functionality
- **PersonValidationTest**: Tests all validation constraints
- Test coverage includes positive and negative test cases
- Error handling and edge cases are thoroughly tested

Run tests with:
```bash
mvn test
```

## Architecture Benefits

1. **Immutable Data**: Records provide immutable data structures
2. **Builder Pattern**: Easy object creation and modification
3. **Type Safety**: Strong typing with LocalDate and LocalDateTime
4. **Validation**: Declarative validation with clear error messages
5. **Clean Logging**: Lombok reduces boilerplate code
6. **Testability**: High test coverage with comprehensive unit tests
7. **JSON Compatibility**: Proper serialization/deserialization handling

## Configuration

The application uses minimal configuration in `application.properties`:
- Logging levels and patterns
- Jackson JSON configuration
- Server port (optional)

This is a standalone Spring Boot application that demonstrates modern Java development practices with clean architecture and comprehensive testing.