# Demo App Property - Spring Boot Property Mapping Example

This Spring Boot 3.5.0 project demonstrates comprehensive property mapping techniques using `@ConfigurationProperties`, including nested classes, lists, maps, and complex configurations. **This version does not use the spring-boot-configuration-processor library** and includes comprehensive unit and integration tests.

## Features Demonstrated

### 1. Basic Property Mapping
- Simple string, boolean, and numeric properties
- Application metadata (name, version, description)

### 2. Nested Class Mapping
- **Server Configuration**: Host, port, SSL settings
- **Database Configuration**: Connection details, pool settings
- **Email Configuration**: SMTP settings
- **Security Configuration**: JWT and password policy settings
- **Monitoring Configuration**: Health checks and alert thresholds

### 3. List Property Mapping
- Allowed origins for CORS
- Admin email addresses
- Supported languages
- Allowed headers

### 4. Map Property Mapping
- External service configurations (URL, API key, timeout)
- Error messages organized by category
- Environment-specific configurations

### 5. Feature Flags
- Boolean flags for enabling/disabling features
- User registration, email notifications, audit logging, caching

## Project Structure

```
src/main/java/com/example/demoappproperty/
├── DemoAppPropertyApplication.java          # Main application class
├── config/
│   └── AppProperties.java                   # Main configuration properties class
├── controller/
│   └── PropertyController.java              # REST endpoints to view properties
└── service/
    └── PropertyDisplayService.java          # Service to display properties in console

src/test/java/com/example/demoappproperty/
├── DemoAppPropertyIntegrationTest.java      # Integration tests
├── config/
│   └── AppPropertiesTest.java               # Unit tests for property mapping
├── controller/
│   └── PropertyControllerTest.java          # Unit tests for REST endpoints
└── service/
    └── PropertyDisplayServiceTest.java      # Unit tests for display service

src/test/resources/
└── application-test.properties              # Test-specific properties
```

## Key Technologies Used

- **Spring Boot 3.5.0**: Latest Spring Boot framework
- **Lombok**: Reduces boilerplate code with annotations like `@Data`
- **@ConfigurationProperties**: Type-safe configuration properties
- **JUnit 5**: Modern testing framework
- **AssertJ**: Fluent assertion library
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Testing support with `@SpringBootTest`, `@WebMvcTest`

## Testing Strategy

### 1. Unit Tests
- **AppPropertiesTest**: Tests property binding with `@TestPropertySource`
- **PropertyControllerTest**: Tests REST endpoints with `@WebMvcTest`
- **PropertyDisplayServiceTest**: Tests console output with `CapturedOutput`

### 2. Integration Tests
- **DemoAppPropertyIntegrationTest**: Full application context testing
- Tests actual HTTP endpoints with `TestRestTemplate`
- Validates complete property mapping in real environment

### 3. Test Coverage
- ✅ Basic property mapping (strings, booleans, numbers)
- ✅ Nested class property mapping
- ✅ List property mapping
- ✅ Map property mapping (external services, error messages, environments)
- ✅ Sensitive data masking in REST responses
- ✅ Console output verification
- ✅ Property validation and initialization
- ✅ HTTP endpoint functionality

## How to Run

1. **Clone or create the project structure** with all the provided files
2. **Place application-test.properties in src/test/resources/**
3. **Navigate to the project directory**
4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
5. **Run all tests**:
   ```bash
   mvn test
   ```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Classes
```bash
# Unit tests for property mapping
mvn test -Dtest=AppPropertiesTest

# Controller tests
mvn test -Dtest=PropertyControllerTest

# Integration tests
mvn test -Dtest=DemoAppPropertyIntegrationTest

# Service tests with output capture
mvn test -Dtest=PropertyDisplayServiceTest
```

### Test Results
All tests validate:
- Property values are correctly mapped from application-test.properties
- Nested objects are properly initialized
- Lists and maps contain expected values
- REST endpoints return correct data
- Sensitive information is properly masked
- Console output contains expected property displays

## Viewing Properties

### Console Output
Properties are automatically displayed in the console when the application starts, thanks to the `PropertyDisplayService` implementing `CommandLineRunner`.

### REST Endpoints
Once the application is running, you can access these endpoints:

- `GET /api/properties` - View all properties (with sensitive data masked)
- `GET /api/properties/basic` - View basic application properties
- `GET /api/properties/features` - View feature flags
- `GET /api/properties/lists` - View list properties
- `GET /api/properties/display` - Trigger console display of properties

### Example URLs
- http://localhost:8080/api/properties
- http://localhost:8080/api/properties/basic
- http://localhost:8080/api/properties/features

## Property Mapping Techniques Shown

### 1. Simple Properties
```properties
app.name=demo-app-property
app.version=1.0.0
app.debug=true
```

### 2. Nested Properties
```properties
app.server.host=localhost
app.server.port=8080
app.server.ssl.enabled=false
```

### 3. List Properties
```properties
app.allowed-origins=http://localhost:3000,https://myapp.com
app.admin-emails=admin@example.com,support@example.com
```

### 4. Map Properties
```properties
app.external-services.payment.url=https://payment.gateway.com
app.external-services.payment.api-key=payment_api_key_123
app.error-messages.validation.required=This field is required
```

## Lombok Annotations Used

- `@Data`: Generates getters, setters, toString, equals, and hashCode
- `@RequiredArgsConstructor`: Generates constructor for final fields
- `@Slf4j`: Provides logger instance

## Security Features

- Sensitive data (passwords, API keys, secrets) are masked in REST responses
- Demonstrates how to handle security-related configuration safely
- Tests verify that sensitive data is properly hidden

## Testing Benefits

1. **Reliability**: Comprehensive tests ensure property mapping works correctly
2. **Regression Prevention**: Tests catch breaking changes during development
3. **Documentation**: Tests serve as executable documentation
4. **Confidence**: Full test coverage provides deployment confidence
5. **Maintainability**: Tests help refactor code safely

## Configuration Benefits

1. **Type Safety**: Compile-time checking of property types
2. **IDE Support**: Auto-completion and validation (when using configuration processor)
3. **Validation**: Can add JSR-303 validation annotations
4. **Nesting**: Organize related properties in nested classes
5. **Collections**: Easy mapping of lists and maps
6. **Documentation**: Self-documenting configuration structure

## Key Differences from Version with Configuration Processor

- **No spring-boot-configuration-processor dependency**: Reduces build dependencies
- **No IDE autocomplete**: Properties won't have IDE support for autocomplete
- **No metadata generation**: No META-INF/spring-configuration-metadata.json file
- **Same functionality**: Property mapping works identically at runtime
- **Comprehensive testing**: Tests validate that everything works without the processor

This project serves as a comprehensive reference for implementing property mapping in Spring Boot applications with best practices for organization, security, testing, and maintainability.**: JWT and password policy settings
- **Monitoring Configuration**: Health checks and alert thresholds

### 3. List Property Mapping
- Allowed origins for CORS
- Admin email addresses
- Supported languages
- Allowed headers

### 4. Map Property Mapping
- External service configurations (URL, API key, timeout)
- Error messages organized by category
- Environment-specific configurations

### 5. Feature Flags
- Boolean flags for enabling/disabling features
- User registration, email notifications, audit logging, caching

## Project Structure

```
src/main/java/com/example/demoappproperty/
├── DemoAppPropertyApplication.java          # Main application class
├── config/
│   └── AppProperties.java                   # Main configuration properties class
├── controller/
│   └── PropertyController.java              # REST endpoints to view properties
└── service/
    └── PropertyDisplayService.java          # Service to display properties in console
```

## Key Technologies Used

- **Spring Boot 3.5.0**: Latest Spring Boot framework
- **Lombok**: Reduces boilerplate code with annotations like `@Data`
- **@ConfigurationProperties**: Type-safe configuration properties
- **CommandLineRunner**: Displays properties on application startup

## How to Run

1. **Clone or create the project structure** with all the provided files
2. **Navigate to the project directory**
3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

## Viewing Properties

### Console Output
Properties are automatically displayed in the console when the application starts, thanks to the `PropertyDisplayService` implementing `CommandLineRunner`.

### REST Endpoints
Once the application is running, you can access these endpoints:

- `GET /api/properties` - View all properties (with sensitive data masked)
- `GET /api/properties/basic` - View basic application properties
- `GET /api/properties/features` - View feature flags
- `GET /api/properties/lists` - View list properties
- `GET /api/properties/display` - Trigger console display of properties

### Example URLs
- http://localhost:8080/api/properties
- http://localhost:8080/api/properties/basic
- http://localhost:8080/api/properties/features

## Property Mapping Techniques Shown

### 1. Simple Properties
```properties
app.name=demo-app-property
app.version=1.0.0
app.debug=true
```

### 2. Nested Properties
```properties
app.server.host=localhost
app.server.port=8080
app.server.ssl.enabled=false
```

### 3. List Properties
```properties
app.allowed-origins=http://localhost:3000,https://myapp.com
app.admin-emails=admin@example.com,support@example.com
```

### 4. Map Properties
```properties
app.external-services.payment.url=https://payment.gateway.com
app.external-services.payment.api-key=payment_api_key_123
app.error-messages.validation.required=This field is required
```

## Lombok Annotations Used

- `@Data`: Generates getters, setters, toString, equals, and hashCode
- `@RequiredArgsConstructor`: Generates constructor for final fields
- `@Slf4j`: Provides logger instance

## Security Features

- Sensitive data (passwords, API keys, secrets) are masked in REST responses
- Demonstrates how to handle security-related configuration safely

## Configuration Benefits

1. **Type Safety**: Compile-time checking of property types
2. **IDE Support**: Auto-completion and validation
3. **Validation**: Can add JSR-303 validation annotations
4. **Nesting**: Organize related properties in nested classes
5. **Collections**: Easy mapping of lists and maps
6. **Documentation**: Self-documenting configuration structure

This project serves as a comprehensive reference for implementing property mapping in Spring Boot applications with best practices for organization, security, and maintainability.

-- Test pass code