# Spring Boot 3.5 JAR Execution

**Created:** 8/9/2025 13:25  
**Updated:** 8/9/2025 14:13  
**Exported:** 8/9/2025 15:56  
**Link:** [https://claude.ai/chat/bb62deed-777d-47a8-98fa-918dba7b8fb0](https://claude.ai/chat/bb62deed-777d-47a8-98fa-918dba7b8fb0)  

## Prompt:
8/9/2025, 1:25:24 PM

how to do i run this in spring boot 3.50 , java 21 : java -jar -cp path/to/custom.jar path/to/your/jarfile.jar fully.qualified.package.Application



## Response:
8/9/2025, 1:25:34 PM

The command you've shown has some issues. Here's how to properly run a Spring Boot application in Spring Boot 3.x with Java 21:

## Standard Spring Boot JAR execution:

```bash
java -jar path/to/your/jarfile.jar
```

That's it! Spring Boot creates "fat JARs" that include all dependencies, so you don't need the `-cp` flag.

## If you need additional JARs on the classpath:

```bash
java -cp path/to/custom.jar:path/to/your/jarfile.jar fully.qualified.package.Application
```

**However**, this approach has problems with Spring Boot fat JARs because they use a special structure and custom classloader.

## Better approaches for additional dependencies:

### 1. **Recommended: Include dependencies in your build**
Add the custom JAR to your `pom.xml` or `build.gradle`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>custom</artifactId>
    <version>1.0.0</version>
    <systemPath>path/to/custom.jar</systemPath>
    <scope>system</scope>
</dependency>
```

### 2. **Use the loader.path property:**
```bash
java -Dloader.path=path/to/custom.jar -jar path/to/your/jarfile.jar
```

### 3. **Specify classpath with Spring Boot's format:**
```bash
java -cp path/to/custom.jar -jar path/to/your/jarfile.jar
```

## For Java 21 specific considerations:
Make sure your `application.properties` or `application.yml` specifies:
```properties
java.version=21
```

And in your `pom.xml`:
```xml
<properties>
    <java.version>21</java.version>
    <spring-boot.version>3.5.0</spring-boot.version>
</properties>
```

The `loader.path` approach (#2 above) is usually the best solution when you need to add external JARs to a Spring Boot application.



## Prompt:
8/9/2025, 1:34:14 PM

what about property launcher, or other launcher in spring boot 3.5.0



## Response:
8/9/2025, 1:34:29 PM

Great question! Spring Boot 3.5.0 provides several launcher options beyond the default `JarLauncher`. Here are the available launchers and how to use them:

## Available Launchers in Spring Boot 3.5.0:

### 1. **JarLauncher** (Default)
- Used for standard fat JAR execution
- Handles nested JARs automatically
```bash
java -jar your-app.jar
```

### 2. **PropertiesLauncher**
- Most flexible launcher for custom classpath scenarios
- Supports external configuration via properties

```bash
java -Dloader.main=com.example.Application -jar your-app.jar
```

**Key PropertiesLauncher properties:**
```bash
# Specify main class
-Dloader.main=fully.qualified.package.Application

# Add external JARs/directories to classpath
-Dloader.path=path/to/custom.jar,path/to/lib/directory

# Set system properties
-Dloader.system=true

# Configure library directory
-Dloader.home=path/to/home

# Add arguments
-Dloader.args=--spring.profiles.active=prod
```

### 3. **WarLauncher**
- For WAR file deployments
- Used when packaging as WAR instead of JAR

### 4. **Custom Launchers**
You can create your own launcher by extending `Launcher` class.

## Using PropertiesLauncher for your use case:

Since you want to add a custom JAR, PropertiesLauncher is perfect:

```bash
java -Dloader.main=fully.qualified.package.Application \
     -Dloader.path=path/to/custom.jar \
     -jar path/to/your/jarfile.jar
```

## Configuring PropertiesLauncher in your JAR:

### Option 1: At build time in `pom.xml`:
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <layout>ZIP</layout>
        <mainClass>fully.qualified.package.Application</mainClass>
    </configuration>
</plugin>
```

### Option 2: Via `loader.properties` file:
Create `src/main/resources/loader.properties`:
```properties
loader.main=fully.qualified.package.Application
loader.path=lib/,custom/
```

### Option 3: Via Manifest (in `MANIFEST.MF`):
```
Main-Class: org.springframework.boot.loader.launch.PropertiesLauncher
Start-Class: fully.qualified.package.Application
```

## Advanced PropertiesLauncher examples:

```bash
# Multiple paths
java -Dloader.path=custom.jar,lib/,config/ -jar app.jar

# With profiles and system properties
java -Dloader.path=custom.jar \
     -Dloader.system=true \
     -Dspring.profiles.active=prod \
     -jar app.jar

# Using home directory
java -Dloader.home=/opt/myapp \
     -Dloader.path=custom/ \
     -jar app.jar
```

## For your specific scenario:

```bash
java -Dloader.main=fully.qualified.package.Application \
     -Dloader.path=path/to/custom.jar \
     -jar path/to/your/jarfile.jar
```

This is much cleaner than trying to manipulate the classpath directly and works perfectly with Spring Boot's architecture in version 3.5.0.



## Prompt:
8/9/2025, 2:13:04 PM

therefore can i have multiple main methods, then create launcher for that specific main at startup, what do you think



## Response:
8/9/2025, 2:13:18 PM

Yes, absolutely! You can have multiple main methods and use different launchers to specify which one to run at startup. This is a great architectural pattern for applications with multiple entry points.

## Multiple Main Classes Approach:

### 1. **Create Multiple Main Classes:**

```java
// Main web application
@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}

// Batch processing application
@SpringBootApplication
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}

// CLI utility application
@SpringBootApplication
public class CliApplication {
    public static void main(String[] args) {
        SpringApplication.run(CliApplication.class, args);
    }
}
```

### 2. **Using PropertiesLauncher to switch between them:**

```bash
# Run web application
java -Dloader.main=com.example.WebApplication -jar myapp.jar

# Run batch processor
java -Dloader.main=com.example.BatchApplication -jar myapp.jar

# Run CLI utility
java -Dloader.main=com.example.CliApplication -jar myapp.jar
```

## Advanced Patterns:

### 3. **Configuration-based switching:**
Create different `loader.properties` files:

**web-loader.properties:**
```properties
loader.main=com.example.WebApplication
loader.path=web-libs/
```

**batch-loader.properties:**
```properties
loader.main=com.example.BatchApplication
loader.path=batch-libs/
```

Then use:
```bash
java -Dloader.config.location=web-loader.properties -jar myapp.jar
```

### 4. **Environment-based launcher selection:**
```bash
# Development mode
java -Dloader.main=com.example.DevApplication \
     -Dspring.profiles.active=dev \
     -jar myapp.jar

# Production mode  
java -Dloader.main=com.example.WebApplication \
     -Dspring.profiles.active=prod \
     -jar myapp.jar
```

### 5. **Script-based launcher wrapper:**
Create shell scripts for each mode:

**start-web.sh:**
```bash
#!/bin/bash
java -Dloader.main=com.example.WebApplication \
     -Dloader.path=web-config/ \
     -jar myapp.jar "$@"
```

**start-batch.sh:**
```bash
#!/bin/bash
java -Dloader.main=com.example.BatchApplication \
     -Dloader.path=batch-config/ \
     -jar myapp.jar "$@"
```

## Benefits of this approach:

1. **Single deployable JAR** with multiple entry points
2. **Shared dependencies** and configuration
3. **Different classpaths** per application mode
4. **Runtime switching** without rebuilding
5. **Clean separation** of concerns

## Alternative: Single main with mode switching:

```java
@SpringBootApplication
public class MultiModeApplication {
    public static void main(String[] args) {
        String mode = System.getProperty("app.mode", "web");
        
        switch (mode) {
            case "batch":
                SpringApplication.run(BatchConfiguration.class, args);
                break;
            case "cli":
                SpringApplication.run(CliConfiguration.class, args);
                break;
            default:
                SpringApplication.run(WebConfiguration.class, args);
        }
    }
}
```

Then run with:
```bash
java -Dapp.mode=batch -jar myapp.jar
```

The PropertiesLauncher approach with multiple main classes is more flexible and aligns well with Spring Boot 3.5.0's architecture. It gives you clean separation and the ability to have different classpaths and configurations per application mode.





---
Powered by [Claude Exporter](https://www.claudexporter.com)