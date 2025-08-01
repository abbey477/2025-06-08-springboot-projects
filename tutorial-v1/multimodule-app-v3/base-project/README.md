# Multi-Module Maven Project with BOM

This is a base multi-module Maven project using Bill of Materials (BOM) for dependency management.

## Project Structure

```
base-project/
├── pom.xml                      (Parent POM with BOM)
├── common-lib/                  (Shared utilities)
├── data-access/                 (Database layer)
└── web-app/                    (Main application)
```

## Key Features

- **BOM Dependency Management**: All library versions centralized in parent `base-project/pom.xml`
- **JUnit 5**: Version 5.9.2 managed consistently across all modules
- **Non-Spring**: Pure Java application with H2 database
- **Real Functionality**: String utils, JSON processing, database operations

## Technologies Used

- Java 11
- Maven 3.x
- JUnit 5 + Mockito
- H2 Database (in-memory)
- HikariCP (connection pooling)
- Jackson (JSON processing)
- Apache Commons Lang
- Google Guava
- OkHttp (HTTP client)
- SLF4J + Logback (logging)

## Quick Start

1. **Build the project:**
   ```bash
   mvn clean compile
   ```

2. **Run tests:**
   ```bash
   mvn test
   ```

3. **Run the application:**
   ```bash
   mvn exec:java -pl web-app
   ```

## Maven Commands

### Build Commands
```bash
mvn clean                    # Clean all modules
mvn compile                  # Compile all modules
mvn test                     # Test all modules
mvn package                  # Package all modules
mvn install                  # Install to local repository
```

### Module-Specific Commands
```bash
mvn test -pl common-lib              # Test only common-lib
mvn compile -pl data-access          # Compile only data-access
mvn package -pl web-app              # Package only web-app
```

### Run Application
```bash
mvn exec:java -pl web-app            # Run from base directory
mvn clean compile exec:java -pl web-app  # Clean, build and run
```

### Dependency Analysis
```bash
mvn dependency:tree                  # Show dependency tree
mvn dependency:tree -pl web-app      # Show web-app dependencies only
mvn help:effective-pom               # Show effective POM (debug BOM)
```

## What the Demo Does

The application demonstrates:
- String manipulation using common-lib utilities
- Database operations with H2 in-memory database  
- JSON serialization/deserialization
- User CRUD operations
- Integration between all modules via BOM

## BOM Benefits

✅ **Centralized Version Management**: All dependency versions in one place
✅ **No Version Conflicts**: BOM ensures consistency across modules  
✅ **Easy Upgrades**: Change version once in BOM, affects all modules
✅ **Module Reusability**: Clean dependencies between modules
✅ **Consistent Testing**: Same JUnit/Mockito versions everywhere

## Module Dependencies

```
web-app → data-access → common-lib
```

All internal dependencies managed through BOM for clean architecture.
