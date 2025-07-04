# Manual Testing Guide for Demo App Property

This guide provides step-by-step instructions for manually testing the Spring Boot property mapping functionality.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- curl (for testing REST endpoints)

## Setup Instructions

1. **Create the project structure:**
   ```
   demo-app-property/
   ├── pom.xml
   ├── src/main/java/com/example/demoappproperty/
   │   ├── DemoAppPropertyApplication.java
   │   ├── config/AppProperties.java
   │   ├── controller/PropertyController.java
   │   └── service/PropertyDisplayService.java
   ├── src/main/resources/
   │   └── application.properties
   └── src/test/
       ├── java/com/example/demoappproperty/
       │   ├── DemoAppPropertyIntegrationTest.java
       │   ├── config/AppPropertiesTest.java
       │   ├── controller/PropertyControllerTest.java
       │   └── service/PropertyDisplayServiceTest.java
       └── resources/
           └── application-test.properties
   ```

2. **Copy all provided files** into their respective locations

## Manual Testing Steps

### Step 1: Verify Project Compilation

```bash
cd demo-app-property
mvn clean compile
```

**Expected Result:** Project compiles without errors

### Step 2: Run Unit Tests

```bash
# Test property mapping
mvn test -Dtest=AppPropertiesTest

# Test controller endpoints
mvn test -Dtest=PropertyControllerTest

# Test service output
mvn test -Dtest=PropertyDisplayServiceTest
```

**Expected Results:**
- All tests pass (green)
- No compilation errors
- Test output shows property values being validated

### Step 3: Run Integration Tests

```bash
mvn test -Dtest=DemoAppPropertyIntegrationTest
```

**Expected Results:**
- Application context loads successfully
- Properties are injected correctly
- REST endpoints work as expected

### Step 4: Run All Tests

```bash
mvn test
```

**Expected Results:**
- All test classes execute
- 100% test success rate
- Property mapping validation complete

### Step 5: Start the Application

```bash
mvn spring-boot:run
```

**Expected Console Output:**
```
=== DEMO APP PROPERTY MAPPING DISPLAY ===
--- BASIC APPLICATION PROPERTIES ---
App Name: demo-app-property
App Version: 1.0.0
App Description: Demo application for property mapping
Debug Mode: true

--- SERVER CONFIGURATION ---
Server Host: localhost
Server Port: 8080
SSL Enabled: false
...

--- LIST PROPERTIES ---
Allowed Origins: [http://localhost:3000, https://myapp.com, https://staging.myapp.com]
Admin Emails: [admin@example.com, support@example.com, manager@example.com]
...

=== END OF PROPERTY MAPPING DISPLAY ===
```

### Step 6: Test REST Endpoints

Open a new terminal and test the endpoints:

#### Basic Properties
```bash
curl http://localhost:8080/api/properties/basic
```

**Expected Response:**
```json
{
  "name": "demo-app-property",
  "version": "1.0.0",
  "description": "Demo application for property mapping",
  "debug": true
}
```

#### Feature Flags
```bash
curl http://localhost:8080/api/properties/features
```

**Expected Response:**
```json
{
  "userRegistration": true,
  "emailNotifications": false,
  "auditLogging": true,
  "cacheEnabled": true
}
```

#### List Properties
```bash
curl http://localhost:8080/api/properties/lists
```

**Expected Response:**
```json
{
  "allowedOrigins": ["http://localhost:3000", "https://myapp.com", "https://staging.myapp.com"],
  "adminEmails": ["admin@example.com", "support@example.com", "manager@example.com"],
  "supportedLanguages": ["en", "es", "fr", "de", "it"],
  "corsAllowedHeaders": ["Content-Type", "Authorization", "X-Requested-With"]
}
```

#### All Properties (with sensitive data masked)
```bash
curl http://localhost:8080/api/properties
```

**Expected Response:** Large JSON with all properties, sensitive fields showing `[HIDDEN]`

#### Trigger Console Display
```bash
curl http://localhost:8080/api/properties/display
```

**Expected Response:**
```
Properties have been displayed in the console. Check the application logs.
```

### Step 7: Verify Property Mapping Types

Check that all these property types work correctly:

#### ✅ Simple Properties
- Strings: `app.name`, `app.version`
- Booleans: `app.debug`, `app.features.user-registration`
- Numbers: `app.server.port`, `app.email.port`

#### ✅ Nested Properties
- Two levels: `app.server.host`
- Three levels: `app.server.ssl.enabled`
- Complex nesting: `app.security.jwt.expiration`

#### ✅ List Properties
- String lists: `app.allowed-origins`
- Comma-separated values converted to List<String>

#### ✅ Map Properties
- Nested maps: `app.external-services.payment.url`
- Double-nested maps: `app.error-messages.validation.required`

### Step 8: Verify Security Features

1. **Check REST endpoint responses** contain `[HIDDEN]` for:
   - Database passwords
   - Email passwords
   - JWT secrets
   - API keys

2. **Check console output** shows `[HIDDEN]` for sensitive data

### Step 9: Test Property Override

1. **Modify application.properties** - change `app.debug=false`
2. **Restart application**
3. **Verify** the change is reflected in console output and REST endpoints

### Step 10: Validate Lombok Integration

Look for these Lombok benefits in the code:

1. **No boilerplate code** in AppProperties.java
2. **@Data annotation** generates getters/setters automatically
3. **@RequiredArgsConstructor** in services
4. **@Slf4j** for logging

## Troubleshooting

### Common Issues and Solutions

#### Tests Fail with "Property not found"
- **Cause:** application-test.properties not in src/test/resources/
- **Solution:** Ensure test properties file is in correct location

#### Application doesn't start
- **Cause:** Port 8080 already in use
- **Solution:** Kill process using port 8080 or change port in properties

#### Properties show null values
- **Cause:** @ConfigurationProperties not being processed
- **Solution:** Ensure @Component annotation is present on AppProperties

#### Sensitive data not masked
- **Cause:** Controller masking logic issue
- **Solution:** Check PropertyController masking methods

#### Console output not showing
- **Cause:** PropertyDisplayService not implementing CommandLineRunner
- **Solution:** Verify @Service annotation and CommandLineRunner implementation

### Validation Checklist

Before considering testing complete, verify:

- [ ] All unit tests pass
- [ ] All integration tests pass
- [ ] Application starts without errors
- [ ] Console displays all property sections
- [ ] REST endpoints return expected data
- [ ] Sensitive data is properly masked
- [ ] Property types are correctly mapped (String, boolean, int, long, List, Map)
- [ ] Nested properties work at multiple levels
- [ ] Lombok reduces code verbosity
- [ ] No spring-boot-configuration-processor dependency needed

## Success Criteria

The manual testing is successful when:

1. **All Tests Pass:** Unit and integration tests execute successfully
2. **Property Mapping Works:** All property types are correctly bound
3. **REST Endpoints Function:** All endpoints return expected JSON
4. **Console Output Complete:** Property display shows all sections
5. **Security Implemented:** Sensitive data is masked appropriately
6. **Lombok Integration:** Code is clean and concise
7. **No Configuration Processor:** Project works without additional dependencies

## Next Steps

After successful manual testing:

1. **Explore Property Customization:** Modify properties and test changes
2. **Add Validation:** Implement JSR-303 validation annotations
3. **Environment Profiles:** Test with different Spring profiles
4. **Custom Property Sources:** Add additional property sources
5. **Property Encryption:** Implement encrypted property values

This completes the comprehensive manual testing of the Spring Boot property mapping demonstration project.