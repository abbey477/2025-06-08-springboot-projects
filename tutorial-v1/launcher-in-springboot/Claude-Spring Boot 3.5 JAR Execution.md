# Spring Boot 3.5 JAR Execution

**Created:** 8/9/2025 13:25  
**Updated:** 8/9/2025 16:26  
**Exported:** 8/9/2025 16:28  
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



## Prompt:
8/9/2025, 4:11:34 PM

create a java class to illustrate starter application with spring boot, non web, standalone



## Response:
8/9/2025, 4:12:09 PM

I'll create a complete standalone Spring Boot application that demonstrates a non-web application structure.

**Standalone Spring Boot Application**

````java
// Main Application Class
package com.example.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StandaloneApplication {

    public static void main(String[] args) {
        // Configure Spring Boot to run as non-web application
        SpringApplication app = new SpringApplication(StandaloneApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        
        ApplicationContext context = app.run(args);
        
        // Optional: Get exit code from context and exit
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("=== Standalone Spring Boot Application Started ===");
            System.out.println("Command line arguments:");
            for (String arg : args) {
                System.out.println(" - " + arg);
            }
            
            System.out.println("\nLoaded beans count: " + ctx.getBeanDefinitionCount());
            System.out.println("Application is running in non-web mode...");
        };
    }
}

// Business Service Component
package com.example.standalone.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DataProcessingService {
    
    @Value("${app.processing.enabled:true}")
    private boolean processingEnabled;
    
    @Value("${app.processing.batch-size:100}")
    private int batchSize;
    
    private int processedRecords = 0;
    
    @PostConstruct
    public void initialize() {
        System.out.println("Initializing DataProcessingService...");
        System.out.println("Processing enabled: " + processingEnabled);
        System.out.println("Batch size: " + batchSize);
    }
    
    public void processData() {
        if (!processingEnabled) {
            System.out.println("Data processing is disabled.");
            return;
        }
        
        System.out.println("Processing batch of " + batchSize + " records...");
        
        // Simulate data processing
        try {
            Thread.sleep(2000); // Simulate processing time
            processedRecords += batchSize;
            System.out.println("✅ Processed " + batchSize + " records. Total: " + processedRecords);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Processing interrupted: " + e.getMessage());
        }
    }
    
    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    public void scheduledProcessing() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        System.out.println("[" + timestamp + "] Running scheduled processing...");
        processData();
    }
    
    public int getTotalProcessedRecords() {
        return processedRecords;
    }
}

// Configuration Class
package com.example.standalone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class ApplicationConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "app.database")
    public DatabaseProperties databaseProperties() {
        return new DatabaseProperties();
    }
    
    // Custom configuration properties
    public static class DatabaseProperties {
        private String url = "jdbc:h2:mem:testdb";
        private String username = "sa";
        private String password = "";
        private int maxConnections = 10;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        
        @Override
        public String toString() {
            return "DatabaseProperties{url='" + url + "', username='" + username + "', maxConnections=" + maxConnections + "}";
        }
    }
}

// Command Line Runner Implementation
package com.example.standalone.runner;

import com.example.standalone.service.DataProcessingService;
import com.example.standalone.config.ApplicationConfig.DatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Execution order if multiple CommandLineRunners exist
public class StartupRunner implements CommandLineRunner {
    
    @Autowired
    private DataProcessingService dataProcessingService;
    
    @Autowired
    private DatabaseProperties databaseProperties;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== StartupRunner Executing ===");
        System.out.println("Database configuration: " + databaseProperties);
        
        // Parse command line arguments
        boolean runOnce = false;
        for (String arg : args) {
            if ("--run-once".equals(arg)) {
                runOnce = true;
                System.out.println("Running in single-execution mode");
                break;
            }
        }
        
        if (runOnce) {
            // Run processing once and exit
            System.out.println("Executing single data processing run...");
            dataProcessingService.processData();
            System.out.println("Single run completed. Total processed: " + 
                             dataProcessingService.getTotalProcessedRecords());
        } else {
            // Continue running with scheduled tasks
            System.out.println("Application will continue running with scheduled tasks...");
            System.out.println("Use Ctrl+C to stop the application");
        }
    }
}

// Application Properties (application.yml)
/*
app:
  processing:
    enabled: true
    batch-size: 150
  database:
    url: jdbc:h2:mem:standalone
    username: admin
    password: secret
    max-connections: 20

logging:
  level:
    com.example.standalone: INFO
    org.springframework.boot: WARN

spring:
  main:
    web-application-type: none
    banner-mode: console
*/

// Maven POM dependencies (pom.xml snippet)
/*
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Remove spring-boot-starter-web to make it non-web -->
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
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
            <configuration>
                <mainClass>com.example.standalone.StandaloneApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
*/
````

This standalone Spring Boot application demonstrates several key concepts for non-web applications:

## Key Features:

1. **Non-Web Configuration**: 
   - Sets `WebApplicationType.NONE` to disable web server
   - Uses `spring-boot-starter` instead of `spring-boot-starter-web`

2. **Command Line Processing**:
   - Implements `CommandLineRunner` for startup logic
   - Parses command line arguments (e.g., `--run-once`)

3. **Scheduled Tasks**:
   - Uses `@EnableScheduling` and `@Scheduled` for background processing
   - Runs tasks every 10 seconds automatically

4. **Configuration Management**:
   - Custom properties with `@ConfigurationProperties`
   - Uses `@Value` for simple property injection

5. **Service Components**:
   - Business logic in `@Service` classes
   - Dependency injection with `@Autowired`

## How to Run:

```bash
# Standard run (with scheduled tasks)
java -jar standalone-app.jar

# Single execution mode
java -jar standalone-app.jar --run-once

# With custom properties
java -jar standalone-app.jar --app.processing.batch-size=200

# Using PropertiesLauncher
java -Dloader.main=com.example.standalone.StandaloneApplication -jar app.jar
```

## Application Behavior:

- **Startup**: Initializes services, loads configuration
- **Standard Mode**: Runs continuously with scheduled tasks every 10 seconds
- **Single Mode**: Processes data once and exits (use `--run-once` flag)
- **Graceful Shutdown**: Use Ctrl+C to stop

This pattern is perfect for batch processors, data migration tools, scheduled jobs, CLI utilities, and any background processing applications that don't need a web interface.



## Prompt:
8/9/2025, 4:15:02 PM

print the process id, thread name .etc



## Response:
8/9/2025, 4:16:02 PM

I'll enhance the standalone Spring Boot application to include process ID, thread information, and other runtime details.

**Standalone Spring Boot Application** ∙ Version 4

````java
// Main Application Class
package com.example.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class StandaloneApplication {

    public static void main(String[] args) {
        // Print startup information
        printSystemInfo();
        
        // Configure Spring Boot to run as non-web application
        SpringApplication app = new SpringApplication(StandaloneApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        
        ApplicationContext context = app.run(args);
        
        // Optional: Get exit code from context and exit
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
    
    private static void printSystemInfo() {
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String hostname = ManagementFactory.getRuntimeMXBean().getName().split("@")[1];
        Thread currentThread = Thread.currentThread();
        
        System.out.println("=== APPLICATION STARTUP INFO ===");
        System.out.println("Process ID (PID): " + processId);
        System.out.println("Hostname: " + hostname);
        System.out.println("Main Thread Name: " + currentThread.getName());
        System.out.println("Main Thread ID: " + currentThread.getId());
        System.out.println("Thread Group: " + currentThread.getThreadGroup().getName());
        System.out.println("JVM Name: " + ManagementFactory.getRuntimeMXBean().getVmName());
        System.out.println("JVM Version: " + ManagementFactory.getRuntimeMXBean().getVmVersion());
        System.out.println("Start Time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Max Memory: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB");
        System.out.println("=====================================\n");
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Thread currentThread = Thread.currentThread();
            
            System.out.println("=== COMMAND LINE RUNNER EXECUTION ===");
            System.out.println("Executing Thread: " + currentThread.getName());
            System.out.println("Thread ID: " + currentThread.getId());
            System.out.println("Thread Priority: " + currentThread.getPriority());
            System.out.println("Thread State: " + currentThread.getState());
            
            System.out.println("\nCommand line arguments:");
            for (String arg : args) {
                System.out.println(" - " + arg);
            }
            
            System.out.println("\nLoaded beans count: " + ctx.getBeanDefinitionCount());
            System.out.println("Application is running in non-web mode...");
            System.out.println("=====================================\n");
        };
    }
}

// Business Service Component
package com.example.standalone.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DataProcessingService {
    
    @Value("${app.processing.enabled:true}")
    private boolean processingEnabled;
    
    @Value("${app.processing.batch-size:100}")
    private int batchSize;
    
    private int processedRecords = 0;
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    
    @PostConstruct
    public void initialize() {
        Thread currentThread = Thread.currentThread();
        
        System.out.println("=== DATA PROCESSING SERVICE INIT ===");
        System.out.println("Initializing Thread: " + currentThread.getName());
        System.out.println("Thread ID: " + currentThread.getId());
        System.out.println("Thread Group: " + currentThread.getThreadGroup().getName());
        System.out.println("Processing enabled: " + processingEnabled);
        System.out.println("Batch size: " + batchSize);
        System.out.println("====================================\n");
    }
    
    public void processData() {
        if (!processingEnabled) {
            System.out.println("Data processing is disabled.");
            return;
        }
        
        Thread currentThread = Thread.currentThread();
        long threadCpuTime = threadMXBean.getCurrentThreadCpuTime();
        
        System.out.println("--- PROCESSING BATCH ---");
        System.out.println("Thread: " + currentThread.getName() + " (ID: " + currentThread.getId() + ")");
        System.out.println("Thread Priority: " + currentThread.getPriority());
        System.out.println("Thread State: " + currentThread.getState());
        System.out.println("Thread CPU Time: " + (threadCpuTime / 1_000_000) + " ms");
        System.out.println("Processing batch of " + batchSize + " records...");
        
        // Simulate data processing
        try {
            Thread.sleep(2000); // Simulate processing time
            processedRecords += batchSize;
            
            long postProcessingCpuTime = threadMXBean.getCurrentThreadCpuTime();
            long cpuTimeUsed = (postProcessingCpuTime - threadCpuTime) / 1_000_000;
            
            System.out.println("✅ Processed " + batchSize + " records. Total: " + processedRecords);
            System.out.println("CPU Time Used: " + cpuTimeUsed + " ms");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Processing interrupted: " + e.getMessage());
        }
        System.out.println("------------------------\n");
    }
    
    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    public void scheduledProcessing() {
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        
        System.out.println("=== SCHEDULED TASK EXECUTION ===");
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Process ID: " + processId);
        System.out.println("Scheduler Thread: " + currentThread.getName());
        System.out.println("Thread ID: " + currentThread.getId());
        System.out.println("Thread Group: " + currentThread.getThreadGroup().getName());
        System.out.println("Is Daemon Thread: " + currentThread.isDaemon());
        
        // Thread pool information
        System.out.println("Active Thread Count: " + Thread.activeCount());
        System.out.println("Total Started Thread Count: " + threadMXBean.getTotalStartedThreadCount());
        
        processData();
        System.out.println("================================\n");
    }
    
    public int getTotalProcessedRecords() {
        return processedRecords;
    }
    
    public void printThreadDump() {
        System.out.println("=== THREAD DUMP ===");
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        while (rootGroup.getParent() != null) {
            rootGroup = rootGroup.getParent();
        }
        
        Thread[] threads = new Thread[rootGroup.activeCount()];
        rootGroup.enumerate(threads);
        
        for (Thread thread : threads) {
            if (thread != null) {
                System.out.println("Thread: " + thread.getName() + 
                                 " | ID: " + thread.getId() + 
                                 " | State: " + thread.getState() + 
                                 " | Priority: " + thread.getPriority() +
                                 " | Daemon: " + thread.isDaemon());
            }
        }
        System.out.println("==================\n");
    }
}

// Configuration Class
package com.example.standalone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class ApplicationConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "app.database")
    public DatabaseProperties databaseProperties() {
        return new DatabaseProperties();
    }
    
    // Custom configuration properties
    public static class DatabaseProperties {
        private String url = "jdbc:h2:mem:testdb";
        private String username = "sa";
        private String password = "";
        private int maxConnections = 10;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        
        @Override
        public String toString() {
            return "DatabaseProperties{url='" + url + "', username='" + username + "', maxConnections=" + maxConnections + "}";
        }
    }
}

// Command Line Runner Implementation
package com.example.standalone.runner;

import com.example.standalone.service.DataProcessingService;
import com.example.standalone.config.ApplicationConfig.DatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.management.ManagementFactory;

@Component
@Order(1) // Execution order if multiple CommandLineRunners exist
public class StartupRunner implements CommandLineRunner {
    
    @Autowired
    private DataProcessingService dataProcessingService;
    
    @Autowired
    private DatabaseProperties databaseProperties;
    
    @Override
    public void run(String... args) throws Exception {
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        
        System.out.println("=== STARTUP RUNNER EXECUTION ===");
        System.out.println("Process ID: " + processId);
        System.out.println("JVM Uptime: " + uptime + " ms");
        System.out.println("Runner Thread: " + currentThread.getName());
        System.out.println("Thread ID: " + currentThread.getId());
        System.out.println("Thread Priority: " + currentThread.getPriority());
        System.out.println("Thread State: " + currentThread.getState());
        System.out.println("Database configuration: " + databaseProperties);
        
        // Parse command line arguments
        boolean runOnce = false;
        boolean showThreads = false;
        
        for (String arg : args) {
            if ("--run-once".equals(arg)) {
                runOnce = true;
                System.out.println("Running in single-execution mode");
            } else if ("--show-threads".equals(arg)) {
                showThreads = true;
                System.out.println("Will display thread information");
            }
        }
        
        if (showThreads) {
            dataProcessingService.printThreadDump();
        }
        
        if (runOnce) {
            // Run processing once and exit
            System.out.println("Executing single data processing run...");
            dataProcessingService.processData();
            System.out.println("Single run completed. Total processed: " + 
                             dataProcessingService.getTotalProcessedRecords());
        } else {
            // Continue running with scheduled tasks
            System.out.println("Application will continue running with scheduled tasks...");
            System.out.println("Use Ctrl+C to stop the application");
            System.out.println("Available arguments: --run-once, --show-threads");
        }
        System.out.println("=================================\n");
    }
}

// Application Properties (application.yml)
/*
app:
  processing:
    enabled: true
    batch-size: 150
  database:
    url: jdbc:h2:mem:standalone
    username: admin
    password: secret
    max-connections: 20

logging:
  level:
    com.example.standalone: INFO
    org.springframework.boot: WARN

spring:
  main:
    web-application-type: none
    banner-mode: console
*/

// Maven POM dependencies (pom.xml snippet)
/*
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Remove spring-boot-starter-web to make it non-web -->
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
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
            <configuration>
                <mainClass>com.example.standalone.StandaloneApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
*/
````

Now the application includes comprehensive process and thread information! Here's what it displays:

## Runtime Information Displayed:

### **At Startup:**
- **Process ID (PID)** - The operating system process identifier
- **Hostname** - The machine name where the application is running
- **Main Thread Name** - Usually "main"
- **Thread ID** - JVM internal thread identifier
- **Thread Group** - Thread grouping information
- **JVM Details** - Name, version, start time
- **System Resources** - Available processors, max memory

### **During Service Initialization:**
- **Initializing Thread** - Which thread is running the @PostConstruct
- **Thread ID and Group** - Thread identification details
- **Configuration Values** - Application properties loaded

### **During Scheduled Execution:**
- **Current Timestamp** - When the scheduled task runs
- **Process ID** - Confirms same process is running
- **Scheduler Thread Name** - Spring's scheduler thread (e.g., "scheduling-1")
- **Thread State and Priority** - Current thread status
- **CPU Time Usage** - How much CPU time the thread consumed
- **Thread Pool Statistics** - Active threads, total started threads

### **During Command Line Runner:**
- **JVM Uptime** - How long the application has been running
- **Runner Thread Details** - Thread executing the startup logic

## New Command Line Options:

```bash
# Show thread dump at startup
java -jar app.jar --show-threads

# Run once with thread information
java -jar app.jar --run-once --show-threads

# Regular run (shows all runtime info automatically)
java -jar app.jar
```

## Sample Output:
```
=== APPLICATION STARTUP INFO ===
Process ID (PID): 12345
Hostname: my-laptop.local
Main Thread Name: main
Thread ID: 1
Thread Group: main
JVM Name: OpenJDK 64-Bit Server VM
JVM Version: 21.0.1+12-29
Start Time: 2025-08-09T14:30:15.123
Available Processors: 8
Max Memory: 1024 MB
=====================================

=== SCHEDULED TASK EXECUTION ===
Timestamp: 14:30:25.456
Process ID: 12345
Scheduler Thread: scheduling-1
Thread ID: 15
Thread Group: main
Is Daemon Thread: true
Active Thread Count: 12
Total Started Thread Count: 18
```

This gives you complete visibility into the application's runtime behavior, thread management, and system resource usage - perfect for monitoring standalone Spring Boot applications in production environments!



## Prompt:
8/9/2025, 4:16:45 PM

change from *System.out.println to log.info*



## Response:
8/9/2025, 4:18:19 PM



**Standalone Spring Boot Application** ∙ Version 8

````java
// Main Application Class
package com.example.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class StandaloneApplication {
    
    private static final Logger log = LoggerFactory.getLogger(StandaloneApplication.class);

    public static void main(String[] args) {
        // Print startup information
        printSystemInfo();
        
        // Configure Spring Boot to run as non-web application
        SpringApplication app = new SpringApplication(StandaloneApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        
        ApplicationContext context = app.run(args);
        
        // Optional: Get exit code from context and exit
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
    
    private static void printSystemInfo() {
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String hostname = ManagementFactory.getRuntimeMXBean().getName().split("@")[1];
        Thread currentThread = Thread.currentThread();
        
        log.info("=== APPLICATION STARTUP INFO ===");
        log.info("Process ID (PID): {}", processId);
        log.info("Hostname: {}", hostname);
        log.info("Main Thread Name: {}", currentThread.getName());
        log.info("Main Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("JVM Name: {}", ManagementFactory.getRuntimeMXBean().getVmName());
        log.info("JVM Version: {}", ManagementFactory.getRuntimeMXBean().getVmVersion());
        log.info("Start Time: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("Available Processors: {}", Runtime.getRuntime().availableProcessors());
        log.info("Max Memory: {} MB", (Runtime.getRuntime().maxMemory() / (1024 * 1024)));
        log.info("=====================================");
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Thread currentThread = Thread.currentThread();
            
            log.info("=== COMMAND LINE RUNNER EXECUTION ===");
            log.info("Executing Thread: {}", currentThread.getName());
            log.info("Thread ID: {}", currentThread.getId());
            log.info("Thread Priority: {}", currentThread.getPriority());
            log.info("Thread State: {}", currentThread.getState());
            
            log.info("Command line arguments:");
            for (String arg : args) {
                log.info(" - {}", arg);
            }
            
            log.info("Loaded beans count: {}", ctx.getBeanDefinitionCount());
            log.info("Application is running in non-web mode...");
            log.info("=====================================");
        };
    }
}

// Business Service Component
package com.example.standalone.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DataProcessingService {
    
    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);
    
    @Value("${app.processing.enabled:true}")
    private boolean processingEnabled;
    
    @Value("${app.processing.batch-size:100}")
    private int batchSize;
    
    private int processedRecords = 0;
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    
    @PostConstruct
    public void initialize() {
        Thread currentThread = Thread.currentThread();
        
        log.info("=== DATA PROCESSING SERVICE INIT ===");
        log.info("Initializing Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("Processing enabled: {}", processingEnabled);
        log.info("Batch size: {}", batchSize);
        log.info("====================================");
    }
    
    public void processData() {
        if (!processingEnabled) {
            log.warn("Data processing is disabled.");
            return;
        }
        
        Thread currentThread = Thread.currentThread();
        long threadCpuTime = threadMXBean.getCurrentThreadCpuTime();
        
        log.info("--- PROCESSING BATCH ---");
        log.info("Thread: {} (ID: {})", currentThread.getName(), currentThread.getId());
        log.info("Thread Priority: {}", currentThread.getPriority());
        log.info("Thread State: {}", currentThread.getState());
        log.info("Thread CPU Time: {} ms", (threadCpuTime / 1_000_000));
        log.info("Processing batch of {} records...", batchSize);
        
        // Simulate data processing
        try {
            Thread.sleep(2000); // Simulate processing time
            processedRecords += batchSize;
            
            long postProcessingCpuTime = threadMXBean.getCurrentThreadCpuTime();
            long cpuTimeUsed = (postProcessingCpuTime - threadCpuTime) / 1_000_000;
            
            log.info("✅ Processed {} records. Total: {}", batchSize, processedRecords);
            log.info("CPU Time Used: {} ms", cpuTimeUsed);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Processing interrupted: {}", e.getMessage());
        }
        log.info("------------------------");
    }
    
    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    public void scheduledProcessing() {
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        
        log.info("=== SCHEDULED TASK EXECUTION ===");
        log.info("Timestamp: {}", timestamp);
        log.info("Process ID: {}", processId);
        log.info("Scheduler Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("Is Daemon Thread: {}", currentThread.isDaemon());
        
        // Thread pool information
        log.info("Active Thread Count: {}", Thread.activeCount());
        log.info("Total Started Thread Count: {}", threadMXBean.getTotalStartedThreadCount());
        
        processData();
        log.info("================================");
    }
    
    public int getTotalProcessedRecords() {
        return processedRecords;
    }
    
    public void printThreadDump() {
        log.info("=== THREAD DUMP ===");
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        while (rootGroup.getParent() != null) {
            rootGroup = rootGroup.getParent();
        }
        
        Thread[] threads = new Thread[rootGroup.activeCount()];
        rootGroup.enumerate(threads);
        
        for (Thread thread : threads) {
            if (thread != null) {
                log.info("Thread: {} | ID: {} | State: {} | Priority: {} | Daemon: {}", 
                        thread.getName(), thread.getId(), thread.getState(), 
                        thread.getPriority(), thread.isDaemon());
            }
        }
        log.info("==================");
    }
}

// Configuration Class
package com.example.standalone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class ApplicationConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "app.database")
    public DatabaseProperties databaseProperties() {
        return new DatabaseProperties();
    }
    
    // Custom configuration properties
    public static class DatabaseProperties {
        private String url = "jdbc:h2:mem:testdb";
        private String username = "sa";
        private String password = "";
        private int maxConnections = 10;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        
        @Override
        public String toString() {
            return "DatabaseProperties{url='" + url + "', username='" + username + "', maxConnections=" + maxConnections + "}";
        }
    }
}

// Command Line Runner Implementation
package com.example.standalone.runner;

import com.example.standalone.service.DataProcessingService;
import com.example.standalone.config.ApplicationConfig.DatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;

@Component
@Order(1) // Execution order if multiple CommandLineRunners exist
public class StartupRunner implements CommandLineRunner {
    
    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);
    
    @Autowired
    private DataProcessingService dataProcessingService;
    
    @Autowired
    private DatabaseProperties databaseProperties;
    
    @Override
    public void run(String... args) throws Exception {
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        
        log.info("=== STARTUP RUNNER EXECUTION ===");
        log.info("Process ID: {}", processId);
        log.info("JVM Uptime: {} ms", uptime);
        log.info("Runner Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Priority: {}", currentThread.getPriority());
        log.info("Thread State: {}", currentThread.getState());
        log.info("Database configuration: {}", databaseProperties);
        
        // Parse command line arguments
        boolean runOnce = false;
        boolean showThreads = false;
        
        for (String arg : args) {
            if ("--run-once".equals(arg)) {
                runOnce = true;
                log.info("Running in single-execution mode");
            } else if ("--show-threads".equals(arg)) {
                showThreads = true;
                log.info("Will display thread information");
            }
        }
        
        if (showThreads) {
            dataProcessingService.printThreadDump();
        }
        
        if (runOnce) {
            // Run processing once and exit
            log.info("Executing single data processing run...");
            dataProcessingService.processData();
            log.info("Single run completed. Total processed: {}", 
                     dataProcessingService.getTotalProcessedRecords());
        } else {
            // Continue running with scheduled tasks
            log.info("Application will continue running with scheduled tasks...");
            log.info("Use Ctrl+C to stop the application");
            log.info("Available arguments: --run-once, --show-threads");
        }
        log.info("=================================");
    }
}

// Application Properties (application.yml)
/*
app:
  processing:
    enabled: true
    batch-size: 150
  database:
    url: jdbc:h2:mem:standalone
    username: admin
    password: secret
    max-connections: 20

logging:
  level:
    com.example.standalone: INFO
    org.springframework.boot: WARN

spring:
  main:
    web-application-type: none
    banner-mode: console
*/

// Maven POM dependencies (pom.xml snippet)
/*
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Remove spring-boot-starter-web to make it non-web -->
    
    <!-- SLF4J logging is included by default in spring-boot-starter -->
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
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
            <configuration>
                <mainClass>com.example.standalone.StandaloneApplication</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
*/
````

Perfect! I've converted all `System.out.println` statements to proper SLF4J logging using `log.info()`, `log.warn()`, and `log.error()`. Here are the key changes:

## Changes Made:

### **1. Added SLF4J Logger to each class:**
```java
private static final Logger log = LoggerFactory.getLogger(ClassName.class);
```

### **2. Converted print statements to appropriate log levels:**
- **`log.info()`** - For general information and process flow
- **`log.warn()`** - For warnings (like "Data processing is disabled")
- **`log.error()`** - For error conditions (like InterruptedException)

### **3. Used parameterized logging:**
Instead of string concatenation:
```java
// Old
System.out.println("Process ID: " + processId);

// New  
log.info("Process ID: {}", processId);
```

### **4. Benefits of this change:**
- **Performance**: Parameterized logging avoids string concatenation when log level is disabled
- **Configurability**: Can control log levels via `application.yml`
- **Structured Output**: Better formatting and timestamps
- **Flexibility**: Can route logs to files, databases, external systems
- **Production Ready**: Proper logging framework for enterprise applications

## Enhanced Logging Configuration:

Add this to your `application.yml` for better log control:

```yaml
logging:
  level:
    com.example.standalone: INFO
    com.example.standalone.service: DEBUG
    org.springframework.boot: WARN
    org.springframework.scheduling: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/standalone-app.log
```

## Sample Log Output:
```
2025-08-09 14:30:15.123 [main] INFO  c.e.s.StandaloneApplication - Process ID (PID): 12345
2025-08-09 14:30:15.124 [main] INFO  c.e.s.StandaloneApplication - Hostname: my-laptop.local
2025-08-09 14:30:25.456 [scheduling-1] INFO  c.e.s.s.DataProcessingService - Scheduler Thread: scheduling-1
2025-08-09 14:30:25.457 [scheduling-1] INFO  c.e.s.s.DataProcessingService - ✅ Processed 150 records. Total: 150
```

Now your application uses professional logging practices suitable for production environments!



## Prompt:
8/9/2025, 4:23:28 PM

put it all together as one project, also with runner shellscript to execute the application



## Response:
8/9/2025, 4:25:05 PM



**Complete Spring Boot Standalone Project**

````markdown
# Spring Boot Standalone Application Project

## Project Structure
```
standalone-app/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── standalone/
│       │               ├── StandaloneApplication.java
│       │               ├── config/
│       │               │   └── ApplicationConfig.java
│       │               ├── service/
│       │               │   └── DataProcessingService.java
│       │               └── runner/
│       │                   └── StartupRunner.java
│       └── resources/
│           ├── application.yml
│           └── logback-spring.xml
├── scripts/
│   ├── start-app.sh
│   ├── start-batch.sh
│   ├── start-debug.sh
│   └── stop-app.sh
└── README.md
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
    <artifactId>standalone-app</artifactId>
    <version>1.0.0</version>
    <name>standalone-app</name>
    <description>Spring Boot Standalone Application</description>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Spring Boot Starter (no web) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- Configuration Processor -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Test Dependencies -->
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
                <configuration>
                    <mainClass>com.example.standalone.StandaloneApplication</mainClass>
                    <layout>ZIP</layout>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## src/main/java/com/example/standalone/StandaloneApplication.java
```java
package com.example.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class StandaloneApplication {
    
    private static final Logger log = LoggerFactory.getLogger(StandaloneApplication.class);

    public static void main(String[] args) {
        // Print startup information
        printSystemInfo();
        
        // Configure Spring Boot to run as non-web application
        SpringApplication app = new SpringApplication(StandaloneApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        
        ApplicationContext context = app.run(args);
        
        // Register shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("=== APPLICATION SHUTDOWN ===");
            log.info("Shutting down gracefully...");
            SpringApplication.exit(context, () -> 0);
        }));
    }
    
    private static void printSystemInfo() {
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String hostname = ManagementFactory.getRuntimeMXBean().getName().split("@")[1];
        Thread currentThread = Thread.currentThread();
        
        log.info("=== APPLICATION STARTUP INFO ===");
        log.info("Process ID (PID): {}", processId);
        log.info("Hostname: {}", hostname);
        log.info("Main Thread Name: {}", currentThread.getName());
        log.info("Main Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("JVM Name: {}", ManagementFactory.getRuntimeMXBean().getVmName());
        log.info("JVM Version: {}", ManagementFactory.getRuntimeMXBean().getVmVersion());
        log.info("Start Time: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("Available Processors: {}", Runtime.getRuntime().availableProcessors());
        log.info("Max Memory: {} MB", (Runtime.getRuntime().maxMemory() / (1024 * 1024)));
        log.info("=====================================");
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Thread currentThread = Thread.currentThread();
            
            log.info("=== COMMAND LINE RUNNER EXECUTION ===");
            log.info("Executing Thread: {}", currentThread.getName());
            log.info("Thread ID: {}", currentThread.getId());
            log.info("Thread Priority: {}", currentThread.getPriority());
            log.info("Thread State: {}", currentThread.getState());
            
            log.info("Command line arguments:");
            for (String arg : args) {
                log.info(" - {}", arg);
            }
            
            log.info("Loaded beans count: {}", ctx.getBeanDefinitionCount());
            log.info("Application is running in non-web mode...");
            log.info("=====================================");
        };
    }
}
```

## src/main/java/com/example/standalone/config/ApplicationConfig.java
```java
package com.example.standalone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

@Configuration
public class ApplicationConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "app.database")
    @Validated
    public DatabaseProperties databaseProperties() {
        return new DatabaseProperties();
    }
    
    @Bean
    @ConfigurationProperties(prefix = "app.processing")
    @Validated
    public ProcessingProperties processingProperties() {
        return new ProcessingProperties();
    }
    
    // Database configuration properties
    public static class DatabaseProperties {
        @NotBlank
        private String url = "jdbc:h2:mem:testdb";
        @NotBlank
        private String username = "sa";
        private String password = "";
        @Min(1)
        private int maxConnections = 10;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        
        @Override
        public String toString() {
            return "DatabaseProperties{url='" + url + "', username='" + username + "', maxConnections=" + maxConnections + "}";
        }
    }
    
    // Processing configuration properties
    public static class ProcessingProperties {
        private boolean enabled = true;
        @Min(1)
        private int batchSize = 100;
        @Min(1000)
        private long scheduleRate = 10000; // milliseconds
        
        // Getters and setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public int getBatchSize() { return batchSize; }
        public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
        
        public long getScheduleRate() { return scheduleRate; }
        public void setScheduleRate(long scheduleRate) { this.scheduleRate = scheduleRate; }
        
        @Override
        public String toString() {
            return "ProcessingProperties{enabled=" + enabled + ", batchSize=" + batchSize + ", scheduleRate=" + scheduleRate + "}";
        }
    }
}
```

## src/main/java/com/example/standalone/service/DataProcessingService.java
```java
package com.example.standalone.service;

import com.example.standalone.config.ApplicationConfig.ProcessingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataProcessingService {
    
    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);
    
    @Autowired
    private ProcessingProperties processingProperties;
    
    private final AtomicInteger processedRecords = new AtomicInteger(0);
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private volatile boolean isShuttingDown = false;
    
    @PostConstruct
    public void initialize() {
        Thread currentThread = Thread.currentThread();
        
        log.info("=== DATA PROCESSING SERVICE INIT ===");
        log.info("Initializing Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("Processing Configuration: {}", processingProperties);
        log.info("====================================");
    }
    
    @PreDestroy
    public void shutdown() {
        isShuttingDown = true;
        log.info("=== DATA PROCESSING SERVICE SHUTDOWN ===");
        log.info("Final processed records count: {}", processedRecords.get());
        log.info("Service shutdown completed");
    }
    
    public void processData() {
        if (!processingProperties.isEnabled()) {
            log.warn("Data processing is disabled.");
            return;
        }
        
        if (isShuttingDown) {
            log.info("Skipping processing - application is shutting down");
            return;
        }
        
        Thread currentThread = Thread.currentThread();
        long threadCpuTime = threadMXBean.getCurrentThreadCpuTime();
        
        log.info("--- PROCESSING BATCH ---");
        log.info("Thread: {} (ID: {})", currentThread.getName(), currentThread.getId());
        log.info("Thread Priority: {}", currentThread.getPriority());
        log.info("Thread State: {}", currentThread.getState());
        log.info("Thread CPU Time: {} ms", (threadCpuTime / 1_000_000));
        log.info("Processing batch of {} records...", processingProperties.getBatchSize());
        
        // Simulate data processing
        try {
            Thread.sleep(2000); // Simulate processing time
            int newTotal = processedRecords.addAndGet(processingProperties.getBatchSize());
            
            long postProcessingCpuTime = threadMXBean.getCurrentThreadCpuTime();
            long cpuTimeUsed = (postProcessingCpuTime - threadCpuTime) / 1_000_000;
            
            log.info("✅ Processed {} records. Total: {}", processingProperties.getBatchSize(), newTotal);
            log.info("CPU Time Used: {} ms", cpuTimeUsed);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Processing interrupted: {}", e.getMessage());
        }
        log.info("------------------------");
    }
    
    @Scheduled(fixedRateString = "#{@processingProperties.scheduleRate}")
    public void scheduledProcessing() {
        if (isShuttingDown) {
            return;
        }
        
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        
        log.info("=== SCHEDULED TASK EXECUTION ===");
        log.info("Timestamp: {}", timestamp);
        log.info("Process ID: {}", processId);
        log.info("Scheduler Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("Is Daemon Thread: {}", currentThread.isDaemon());
        
        // Thread pool information
        log.info("Active Thread Count: {}", Thread.activeCount());
        log.info("Total Started Thread Count: {}", threadMXBean.getTotalStartedThreadCount());
        
        processData();
        log.info("================================");
    }
    
    public int getTotalProcessedRecords() {
        return processedRecords.get();
    }
    
    public void printThreadDump() {
        log.info("=== THREAD DUMP ===");
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        while (rootGroup.getParent() != null) {
            rootGroup = rootGroup.getParent();
        }
        
        Thread[] threads = new Thread[rootGroup.activeCount()];
        rootGroup.enumerate(threads);
        
        for (Thread thread : threads) {
            if (thread != null) {
                log.info("Thread: {} | ID: {} | State: {} | Priority: {} | Daemon: {}", 
                        thread.getName(), thread.getId(), thread.getState(), 
                        thread.getPriority(), thread.isDaemon());
            }
        }
        log.info("==================");
    }
}
```

## src/main/java/com/example/standalone/runner/StartupRunner.java
```java
package com.example.standalone.runner;

import com.example.standalone.service.DataProcessingService;
import com.example.standalone.config.ApplicationConfig.DatabaseProperties;
import com.example.standalone.config.ApplicationConfig.ProcessingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;

@Component
@Order(1) // Execution order if multiple CommandLineRunners exist
public class StartupRunner implements CommandLineRunner {
    
    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);
    
    @Autowired
    private DataProcessingService dataProcessingService;
    
    @Autowired
    private DatabaseProperties databaseProperties;
    
    @Autowired
    private ProcessingProperties processingProperties;
    
    @Override
    public void run(String... args) throws Exception {
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        
        log.info("=== STARTUP RUNNER EXECUTION ===");
        log.info("Process ID: {}", processId);
        log.info("JVM Uptime: {} ms", uptime);
        log.info("Runner Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Priority: {}", currentThread.getPriority());
        log.info("Thread State: {}", currentThread.getState());
        log.info("Database configuration: {}", databaseProperties);
        log.info("Processing configuration: {}", processingProperties);
        
        // Parse command line arguments
        boolean runOnce = false;
        boolean showThreads = false;
        boolean debugMode = false;
        
        for (String arg : args) {
            switch (arg) {
                case "--run-once":
                    runOnce = true;
                    log.info("Running in single-execution mode");
                    break;
                case "--show-threads":
                    showThreads = true;
                    log.info("Will display thread information");
                    break;
                case "--debug":
                    debugMode = true;
                    log.info("Debug mode enabled");
                    break;
            }
        }
        
        if (showThreads) {
            dataProcessingService.printThreadDump();
        }
        
        if (runOnce) {
            // Run processing once and exit
            log.info("Executing single data processing run...");
            dataProcessingService.processData();
            log.info("Single run completed. Total processed: {}", 
                     dataProcessingService.getTotalProcessedRecords());
        } else {
            // Continue running with scheduled tasks
            log.info("Application will continue running with scheduled tasks...");
            log.info("Use Ctrl+C to stop the application");
            log.info("Available arguments: --run-once, --show-threads, --debug");
        }
        log.info("=================================");
    }
}
```

## src/main/resources/application.yml
```yaml
# Application Configuration
app:
  processing:
    enabled: true
    batch-size: 150
    schedule-rate: 10000  # 10 seconds in milliseconds
  database:
    url: jdbc:h2:mem:standalone
    username: admin
    password: secret
    max-connections: 20

# Spring Configuration
spring:
  application:
    name: standalone-app
  main:
    web-application-type: none
    banner-mode: console
  profiles:
    active: default

# Logging Configuration
logging:
  level:
    com.example.standalone: INFO
    com.example.standalone.service: DEBUG
    org.springframework.boot: WARN
    org.springframework.scheduling: INFO
    org.springframework.context: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/standalone-app.log
    max-size: 10MB
    max-history: 30

# Management and Monitoring
management:
  endpoints:
    enabled-by-default: false
  endpoint:
    health:
      enabled: true
    info:
      enabled: true

---
# Development Profile
spring:
  config:
    activate:
      on-profile: dev
  
app:
  processing:
    schedule-rate: 5000  # 5 seconds for development

logging:
  level:
    com.example.standalone: DEBUG
    org.springframework: DEBUG

---
# Production Profile
spring:
  config:
    activate:
      on-profile: prod

app:
  processing:
    batch-size: 500
    schedule-rate: 30000  # 30 seconds for production

logging:
  level:
    com.example.standalone: INFO
    org.springframework: WARN
  file:
    name: /var/log/standalone-app/app.log
```

## src/main/resources/logback-spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/standalone-app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>logs/standalone-app.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- Async Appender for better performance -->
    <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="FILE"/>
        <queueSize>256</queueSize>
        <discardingThreshold>0</discardingThreshold>
    </appender>
    
    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ASYNC_FILE"/>
    </root>
    
    <!-- Application specific loggers -->
    <logger name="com.example.standalone" level="INFO" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ASYNC_FILE"/>
    </logger>
    
    <!-- Profile-specific configurations -->
    <springProfile name="dev">
        <logger name="com.example.standalone" level="DEBUG"/>
    </springProfile>
    
    <springProfile name="prod">
        <logger name="com.example.standalone" level="INFO"/>
        <logger name="org.springframework" level="WARN"/>
    </springProfile>
</configuration>
```

## scripts/start-app.sh
```bash
#!/bin/bash

# Spring Boot Standalone Application Launcher Script
# Usage: ./start-app.sh [options]

set -e

# Configuration
APP_NAME="standalone-app"
JAR_FILE="../target/${APP_NAME}-1.0.0.jar"
PID_FILE="../${APP_NAME}.pid"
LOG_DIR="../logs"
JAVA_OPTS="-Xms256m -Xmx1024m -XX:+UseG1GC"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_java() {
    if ! command -v java &> /dev/null; then
        log_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
    log_info "Using Java version: $JAVA_VERSION"
}

check_jar() {
    if [ ! -f "$JAR_FILE" ]; then
        log_error "JAR file not found: $JAR_FILE"
        log_info "Please run 'mvn clean package' first"
        exit 1
    fi
}

create_directories() {
    mkdir -p "$LOG_DIR"
    log_info "Created log directory: $LOG_DIR"
}

check_if_running() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p "$PID" > /dev/null 2>&1; then
            log_warn "Application is already running with PID: $PID"
            return 0
        else
            log_warn "Stale PID file found. Removing..."
            rm -f "$PID_FILE"
        fi
    fi
    return 1
}

start_application() {
    local profile=""
    local extra_args=""
    
    # Parse arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            --profile=*)
                profile="${1#*=}"
                shift
                ;;
            --run-once)
                extra_args="$extra_args --run-once"
                shift
                ;;
            --show-threads)
                extra_args="$extra_args --show-threads"
                shift
                ;;
            --debug)
                extra_args="$extra_args --debug"
                JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
                shift
                ;;
            *)
                extra_args="$extra_args $1"
                shift
                ;;
        esac
    done
    
    # Set profile
    if [ -n "$profile" ]; then
        JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=$profile"
        log_info "Using profile: $profile"
    fi
    
    log_info "Starting $APP_NAME..."
    log_info "JAR file: $JAR_FILE"
    log_info "Java options: $JAVA_OPTS"
    log_info "Extra arguments: $extra_args"
    
    # Start application
    nohup java $JAVA_OPTS -jar "$JAR_FILE" $extra_args > "$LOG_DIR/console.log" 2>&1 &
    
    # Save PID
    echo $! > "$PID_FILE"
    PID=$(cat "$PID_FILE")
    
    log_info "Application started with PID: $PID"
    log_info "Console output: $LOG_DIR/console.log"
    log_info "Application logs: $LOG_DIR/standalone-app.log"
    
    # Wait a moment and check if it's still running
    sleep 3
    if ps -p "$PID" > /dev/null 2>&1; then
        log_info "Application is running successfully!"
    else
        log_error "Application failed to start. Check logs for details."
        rm -f "$PID_FILE"
        exit 1
    fi
}

show_usage() {
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --profile=PROFILE    Set Spring profile (dev, prod)"
    echo "  --run-once          Run once and exit"
    echo "  --show-threads      Show thread information"
    echo "  --debug             Enable debug mode with remote debugging"
    echo ""
    echo "Examples:"
    echo "  $0                          # Start with default profile"
    echo "  $0 --profile=dev            # Start with dev profile"
    echo "  $0 --run-once               # Run once and exit"
    echo "  $0 --debug --profile=dev    # Start in debug mode with dev profile"
}

# Main execution
main() {
    log_info "=== Spring Boot Standalone Application Launcher ==="
    
    if [[ $# -eq 1 && ($1 == "-h" || $1 == "--help") ]]; then
        show_usage
        exit 0
    fi
    
    check_java
    check_jar
    create_directories
    
    if check_if_running; then
        exit 1
    fi
    
    start_application "$@"
}

# Run main function with all arguments
main "$@"
```

## scripts/start-batch.sh
```bash
#!/bin/bash

# Batch Processing Mode Launcher
# Runs the application once and exits

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Starting application in batch mode..."
"$SCRIPT_DIR/start-app.sh" --run-once --profile=prod "$@"
```

## scripts/start-debug.sh
```bash
#!/bin/bash

# Debug Mode Launcher
# Starts application with remote debugging enabled

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Starting application in debug mode..."
echo "Remote debugging will be available on port 5005"
"$SCRIPT_DIR/start-app.sh" --debug --profile=dev --show-threads "$@"
```

## scripts/stop-app.sh
```bash
#!/bin/bash

# Spring Boot Standalone Application Stop Script

set -e

# Configuration
APP_NAME="standalone-app"
PID_FILE="../${APP_NAME}.pid"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
````



## Prompt:
8/9/2025, 4:25:16 PM

Continue



## Response:
8/9/2025, 4:26:26 PM



**Complete Spring Boot Standalone Project** ∙ Version 3

````markdown
# Spring Boot Standalone Application Project

## Project Structure
```

## Makefile
```makefile
# Spring Boot Standalone Application Makefile

.PHONY: help clean compile package run run-dev run-prod run-batch run-debug stop status test

# Default goal
.DEFAULT_GOAL := help

# Variables
APP_NAME = standalone-app
VERSION = 1.0.0
JAR_FILE = target/$(APP_NAME)-$(VERSION).jar
SCRIPTS_DIR = scripts

help: ## Show this help message
	@echo "Spring Boot Standalone Application"
	@echo "=================================="
	@echo ""
	@echo "Available targets:"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $1, $2}' $(MAKEFILE_LIST)

clean: ## Clean build artifacts
	@echo "Cleaning build artifacts..."
	mvn clean
	rm -rf logs/
	rm -f $(APP_NAME).pid

compile: ## Compile the application
	@echo "Compiling application..."
	mvn compile

test: ## Run tests
	@echo "Running tests..."
	mvn test

package: clean ## Package the application
	@echo "Packaging application..."
	mvn clean package -DskipTests
	@echo "JAR file created: $(JAR_FILE)"

run: package ## Build and run application (default profile)
	@echo "Starting application..."
	cd $(SCRIPTS_DIR) && ./start-app.sh

run-dev: package ## Build and run application (dev profile)
	@echo "Starting application in development mode..."
	cd $(SCRIPTS_DIR) && ./start-app.sh --profile=dev

run-prod: package ## Build and run application (prod profile)
	@echo "Starting application in production mode..."
	cd $(SCRIPTS_DIR) && ./start-app.sh --profile=prod

run-batch: package ## Run application in batch mode (once and exit)
	@echo "Running application in batch mode..."
	cd $(SCRIPTS_DIR) && ./start-batch.sh

run-debug: package ## Run application in debug mode
	@echo "Starting application in debug mode..."
	cd $(SCRIPTS_DIR) && ./start-debug.sh

stop: ## Stop the running application
	@echo "Stopping application..."
	cd $(SCRIPTS_DIR) && ./stop-app.sh

status: ## Check application status
	@if [ -f "$(APP_NAME).pid" ]; then \
		PID=$(cat $(APP_NAME).pid); \
		if ps -p $PID > /dev/null 2>&1; then \
			echo "Application is running with PID: $PID"; \
		else \
			echo "Application is not running (stale PID file)"; \
		fi \
	else \
		echo "Application is not running"; \
	fi

logs: ## Show application logs
	@if [ -f "logs/standalone-app.log" ]; then \
		tail -f logs/standalone-app.log; \
	else \
		echo "Log file not found"; \
	fi

install: package ## Install the application (copy to /opt)
	@echo "Installing application to /opt/$(APP_NAME)..."
	sudo mkdir -p /opt/$(APP_NAME)
	sudo cp $(JAR_FILE) /opt/$(APP_NAME)/
	sudo cp -r $(SCRIPTS_DIR) /opt/$(APP_NAME)/
	sudo cp src/main/resources/application.yml /opt/$(APP_NAME)/
	@echo "Installation completed"
```

## README.md
```markdown
# Spring Boot Standalone Application

A comprehensive Spring Boot 3.5.0 application template for standalone, non-web applications with Java 21.

## Features

- ✅ **Non-web application** - Lightweight, no web server
- ✅ **Scheduled tasks** - Background processing with configurable intervals
- ✅ **Multiple execution modes** - Batch, continuous, debug modes
- ✅ **Process monitoring** - PID tracking, thread information, system metrics
- ✅ **Professional logging** - SLF4J with Logback, file rotation, async logging
- ✅ **Configuration management** - YAML configuration, profiles, validation
- ✅ **Shell scripts** - Easy deployment and management scripts
- ✅ **Multiple launchers** - PropertiesLauncher support for custom classpath

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher

### Build and Run
```bash
# Clone and build
git clone <repository-url>
cd standalone-app

# Build and run (using Makefile)
make run

# Or build and run manually
mvn clean package
cd scripts
./start-app.sh
```

## Usage

### Available Commands (Makefile)
```bash
make help           # Show available commands
make clean          # Clean build artifacts
make package        # Build JAR file
make run            # Build and run (default profile)
make run-dev        # Run with development profile
make run-prod       # Run with production profile
make run-batch      # Run once and exit
make run-debug      # Run with remote debugging
make stop           # Stop the application
make status         # Check if running
make logs           # Follow application logs
make test           # Run unit tests
```

### Direct Script Usage
```bash
cd scripts

# Standard startup
./start-app.sh

# With different profiles
./start-app.sh --profile=dev
./start-app.sh --profile=prod

# Batch mode (run once and exit)
./start-batch.sh

# Debug mode (remote debugging on port 5005)
./start-debug.sh

# Additional options
./start-app.sh --run-once --show-threads --debug

# Stop application
./stop-app.sh
```

### Java Command Line Options
```bash
# Using PropertiesLauncher
java -Dloader.main=com.example.standalone.StandaloneApplication \
     -Dloader.path=custom.jar \
     -jar standalone-app-1.0.0.jar

# With custom properties
java -jar standalone-app-1.0.0.jar \
     --app.processing.batch-size=500 \
     --spring.profiles.active=prod
```

## Configuration

### Application Properties (application.yml)
```yaml
app:
  processing:
    enabled: true
    batch-size: 150
    schedule-rate: 10000  # milliseconds
  database:
    url: jdbc:h2:mem:standalone
    username: admin
    password: secret
    max-connections: 20
```

### Profiles
- **default** - Standard configuration
- **dev** - Development mode (5-second intervals, debug logging)
- **prod** - Production mode (30-second intervals, optimized settings)

### Logging Configuration
- Console output with colored formatting
- File logging with rotation (10MB files, 30 days retention)
- Async logging for better performance
- Profile-specific log levels

## Architecture

### Project Structure
```
standalone-app/
├── src/main/java/com/example/standalone/
│   ├── StandaloneApplication.java      # Main application class
│   ├── config/ApplicationConfig.java   # Configuration classes
│   ├── service/DataProcessingService.java # Business logic
│   └── runner/StartupRunner.java       # Startup logic
├── src/main/resources/
│   ├── application.yml                 # Configuration
│   └── logback-spring.xml             # Logging configuration
├── scripts/                           # Deployment scripts
└── target/                           # Built artifacts
```

### Key Components

1. **StandaloneApplication** - Main entry point with system information logging
2. **DataProcessingService** - Business logic with scheduled processing
3. **StartupRunner** - Command line argument processing and startup logic
4. **ApplicationConfig** - Configuration properties with validation

## Monitoring and Debugging

### Runtime Information
The application logs comprehensive runtime information:
- Process ID (PID) and hostname
- Thread information (names, IDs, states)
- JVM details (version, memory, processors)
- CPU usage per processing cycle
- Thread pool statistics

### Log Files
- `logs/standalone-app.log` - Application logs with rotation
- `logs/console.log` - Console output when running as daemon

### Process Management
- PID file tracking (`standalone-app.pid`)
- Graceful shutdown handling
- Process status monitoring

## Development

### Adding Custom Launchers
```java
// Create multiple main classes
@SpringBootApplication
public class CustomApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomApplication.class, args);
    }
}

// Use PropertiesLauncher
java -Dloader.main=com.example.standalone.CustomApplication -jar app.jar
```

### Extending Functionality
1. Add new services in `service/` package
2. Create additional configuration properties
3. Implement new CommandLineRunner classes
4. Add custom scheduled tasks

### Testing
```bash
# Run all tests
make test

# Run specific test
mvn test -Dtest=DataProcessingServiceTest
```

## Deployment

### Local Installation
```bash
make install  # Installs to /opt/standalone-app
```

### Docker Deployment
```dockerfile
FROM openjdk:21-jre-slim

COPY target/standalone-app-1.0.0.jar /app/app.jar
COPY scripts/ /app/scripts/

WORKDIR /app
CMD ["java", "-jar", "app.jar"]
```

### Systemd Service
```ini
[Unit]
Description=Spring Boot Standalone Application
After=network.target

[Service]
Type=forking
User=appuser
WorkingDirectory=/opt/standalone-app
ExecStart=/opt/standalone-app/scripts/start-app.sh --profile=prod
ExecStop=/opt/standalone-app/scripts/stop-app.sh
PIDFile=/opt/standalone-app/standalone-app.pid

[Install]
WantedBy=multi-user.target
```

## Troubleshooting

### Common Issues

1. **Application won't start**
   - Check Java version: `java -version`
   - Verify JAR file exists: `ls -la target/`
   - Check logs: `tail -f logs/standalone-app.log`

2. **Port conflicts (debug mode)**
   - Debug port 5005 might be in use
   - Change debug port: `JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5006"`

3. **Permission errors**
   - Make scripts executable: `chmod +x scripts/*.sh`
   - Check log directory permissions

### Log Analysis
```bash
# Follow real-time logs
make logs

# Search for errors
grep ERROR logs/standalone-app.log

# Check startup sequence
grep "STARTUP\|INIT" logs/standalone-app.log
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
```

## .gitignore
```gitignore
# Maven
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

# IDE
.idea/
*.iws
*.iml
*.ipr
.vscode/
*.swp
*.swo

# Logs
logs/
*.log

# Runtime
*.pid
nohup.out

# OS
.DS_Store
Thumbs.db

# Application specific
standalone-app.pid
console.log
```

This complete project provides:

## 🚀 **Key Features:**
- **Professional Structure** - Maven-based with proper package organization
- **Multiple Execution Modes** - Batch, continuous, debug modes
- **Shell Scripts** - Complete deployment automation
- **Configuration Management** - YAML with profiles and validation
- **Process Monitoring** - PID tracking, thread dumps, system metrics
- **Professional Logging** - SLF4J with file rotation and async logging
- **Easy Deployment** - Makefile with common tasks
- **Production Ready** - Systemd service, Docker support

## 📁 **Usage Examples:**
```bash
# Quick start
make run

# Development mode
make run-dev

# Production deployment
make run-prod

# Batch processing
make run-batch

# Debug with IDE
make run-debug

# Stop application
make stop
```

The project is completely self-contained and production-ready for standalone Spring Boot applications!
standalone-app/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── standalone/
│       │               ├── StandaloneApplication.java
│       │               ├── config/
│       │               │   └── ApplicationConfig.java
│       │               ├── service/
│       │               │   └── DataProcessingService.java
│       │               └── runner/
│       │                   └── StartupRunner.java
│       └── resources/
│           ├── application.yml
│           └── logback-spring.xml
├── scripts/
│   ├── start-app.sh
│   ├── start-batch.sh
│   ├── start-debug.sh
│   └── stop-app.sh
└── README.md
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
    <artifactId>standalone-app</artifactId>
    <version>1.0.0</version>
    <name>standalone-app</name>
    <description>Spring Boot Standalone Application</description>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Spring Boot Starter (no web) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- Configuration Processor -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Test Dependencies -->
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
                <configuration>
                    <mainClass>com.example.standalone.StandaloneApplication</mainClass>
                    <layout>ZIP</layout>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## src/main/java/com/example/standalone/StandaloneApplication.java
```java
package com.example.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class StandaloneApplication {
    
    private static final Logger log = LoggerFactory.getLogger(StandaloneApplication.class);

    public static void main(String[] args) {
        // Print startup information
        printSystemInfo();
        
        // Configure Spring Boot to run as non-web application
        SpringApplication app = new SpringApplication(StandaloneApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        
        ApplicationContext context = app.run(args);
        
        // Register shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("=== APPLICATION SHUTDOWN ===");
            log.info("Shutting down gracefully...");
            SpringApplication.exit(context, () -> 0);
        }));
    }
    
    private static void printSystemInfo() {
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String hostname = ManagementFactory.getRuntimeMXBean().getName().split("@")[1];
        Thread currentThread = Thread.currentThread();
        
        log.info("=== APPLICATION STARTUP INFO ===");
        log.info("Process ID (PID): {}", processId);
        log.info("Hostname: {}", hostname);
        log.info("Main Thread Name: {}", currentThread.getName());
        log.info("Main Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("JVM Name: {}", ManagementFactory.getRuntimeMXBean().getVmName());
        log.info("JVM Version: {}", ManagementFactory.getRuntimeMXBean().getVmVersion());
        log.info("Start Time: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("Available Processors: {}", Runtime.getRuntime().availableProcessors());
        log.info("Max Memory: {} MB", (Runtime.getRuntime().maxMemory() / (1024 * 1024)));
        log.info("=====================================");
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Thread currentThread = Thread.currentThread();
            
            log.info("=== COMMAND LINE RUNNER EXECUTION ===");
            log.info("Executing Thread: {}", currentThread.getName());
            log.info("Thread ID: {}", currentThread.getId());
            log.info("Thread Priority: {}", currentThread.getPriority());
            log.info("Thread State: {}", currentThread.getState());
            
            log.info("Command line arguments:");
            for (String arg : args) {
                log.info(" - {}", arg);
            }
            
            log.info("Loaded beans count: {}", ctx.getBeanDefinitionCount());
            log.info("Application is running in non-web mode...");
            log.info("=====================================");
        };
    }
}
```

## src/main/java/com/example/standalone/config/ApplicationConfig.java
```java
package com.example.standalone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

@Configuration
public class ApplicationConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "app.database")
    @Validated
    public DatabaseProperties databaseProperties() {
        return new DatabaseProperties();
    }
    
    @Bean
    @ConfigurationProperties(prefix = "app.processing")
    @Validated
    public ProcessingProperties processingProperties() {
        return new ProcessingProperties();
    }
    
    // Database configuration properties
    public static class DatabaseProperties {
        @NotBlank
        private String url = "jdbc:h2:mem:testdb";
        @NotBlank
        private String username = "sa";
        private String password = "";
        @Min(1)
        private int maxConnections = 10;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public int getMaxConnections() { return maxConnections; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        
        @Override
        public String toString() {
            return "DatabaseProperties{url='" + url + "', username='" + username + "', maxConnections=" + maxConnections + "}";
        }
    }
    
    // Processing configuration properties
    public static class ProcessingProperties {
        private boolean enabled = true;
        @Min(1)
        private int batchSize = 100;
        @Min(1000)
        private long scheduleRate = 10000; // milliseconds
        
        // Getters and setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public int getBatchSize() { return batchSize; }
        public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
        
        public long getScheduleRate() { return scheduleRate; }
        public void setScheduleRate(long scheduleRate) { this.scheduleRate = scheduleRate; }
        
        @Override
        public String toString() {
            return "ProcessingProperties{enabled=" + enabled + ", batchSize=" + batchSize + ", scheduleRate=" + scheduleRate + "}";
        }
    }
}
```

## src/main/java/com/example/standalone/service/DataProcessingService.java
```java
package com.example.standalone.service;

import com.example.standalone.config.ApplicationConfig.ProcessingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataProcessingService {
    
    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);
    
    @Autowired
    private ProcessingProperties processingProperties;
    
    private final AtomicInteger processedRecords = new AtomicInteger(0);
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private volatile boolean isShuttingDown = false;
    
    @PostConstruct
    public void initialize() {
        Thread currentThread = Thread.currentThread();
        
        log.info("=== DATA PROCESSING SERVICE INIT ===");
        log.info("Initializing Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("Processing Configuration: {}", processingProperties);
        log.info("====================================");
    }
    
    @PreDestroy
    public void shutdown() {
        isShuttingDown = true;
        log.info("=== DATA PROCESSING SERVICE SHUTDOWN ===");
        log.info("Final processed records count: {}", processedRecords.get());
        log.info("Service shutdown completed");
    }
    
    public void processData() {
        if (!processingProperties.isEnabled()) {
            log.warn("Data processing is disabled.");
            return;
        }
        
        if (isShuttingDown) {
            log.info("Skipping processing - application is shutting down");
            return;
        }
        
        Thread currentThread = Thread.currentThread();
        long threadCpuTime = threadMXBean.getCurrentThreadCpuTime();
        
        log.info("--- PROCESSING BATCH ---");
        log.info("Thread: {} (ID: {})", currentThread.getName(), currentThread.getId());
        log.info("Thread Priority: {}", currentThread.getPriority());
        log.info("Thread State: {}", currentThread.getState());
        log.info("Thread CPU Time: {} ms", (threadCpuTime / 1_000_000));
        log.info("Processing batch of {} records...", processingProperties.getBatchSize());
        
        // Simulate data processing
        try {
            Thread.sleep(2000); // Simulate processing time
            int newTotal = processedRecords.addAndGet(processingProperties.getBatchSize());
            
            long postProcessingCpuTime = threadMXBean.getCurrentThreadCpuTime();
            long cpuTimeUsed = (postProcessingCpuTime - threadCpuTime) / 1_000_000;
            
            log.info("✅ Processed {} records. Total: {}", processingProperties.getBatchSize(), newTotal);
            log.info("CPU Time Used: {} ms", cpuTimeUsed);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Processing interrupted: {}", e.getMessage());
        }
        log.info("------------------------");
    }
    
    @Scheduled(fixedRateString = "#{@processingProperties.scheduleRate}")
    public void scheduledProcessing() {
        if (isShuttingDown) {
            return;
        }
        
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        
        log.info("=== SCHEDULED TASK EXECUTION ===");
        log.info("Timestamp: {}", timestamp);
        log.info("Process ID: {}", processId);
        log.info("Scheduler Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Group: {}", currentThread.getThreadGroup().getName());
        log.info("Is Daemon Thread: {}", currentThread.isDaemon());
        
        // Thread pool information
        log.info("Active Thread Count: {}", Thread.activeCount());
        log.info("Total Started Thread Count: {}", threadMXBean.getTotalStartedThreadCount());
        
        processData();
        log.info("================================");
    }
    
    public int getTotalProcessedRecords() {
        return processedRecords.get();
    }
    
    public void printThreadDump() {
        log.info("=== THREAD DUMP ===");
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        while (rootGroup.getParent() != null) {
            rootGroup = rootGroup.getParent();
        }
        
        Thread[] threads = new Thread[rootGroup.activeCount()];
        rootGroup.enumerate(threads);
        
        for (Thread thread : threads) {
            if (thread != null) {
                log.info("Thread: {} | ID: {} | State: {} | Priority: {} | Daemon: {}", 
                        thread.getName(), thread.getId(), thread.getState(), 
                        thread.getPriority(), thread.isDaemon());
            }
        }
        log.info("==================");
    }
}
```

## src/main/java/com/example/standalone/runner/StartupRunner.java
```java
package com.example.standalone.runner;

import com.example.standalone.service.DataProcessingService;
import com.example.standalone.config.ApplicationConfig.DatabaseProperties;
import com.example.standalone.config.ApplicationConfig.ProcessingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;

@Component
@Order(1) // Execution order if multiple CommandLineRunners exist
public class StartupRunner implements CommandLineRunner {
    
    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);
    
    @Autowired
    private DataProcessingService dataProcessingService;
    
    @Autowired
    private DatabaseProperties databaseProperties;
    
    @Autowired
    private ProcessingProperties processingProperties;
    
    @Override
    public void run(String... args) throws Exception {
        Thread currentThread = Thread.currentThread();
        String processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        
        log.info("=== STARTUP RUNNER EXECUTION ===");
        log.info("Process ID: {}", processId);
        log.info("JVM Uptime: {} ms", uptime);
        log.info("Runner Thread: {}", currentThread.getName());
        log.info("Thread ID: {}", currentThread.getId());
        log.info("Thread Priority: {}", currentThread.getPriority());
        log.info("Thread State: {}", currentThread.getState());
        log.info("Database configuration: {}", databaseProperties);
        log.info("Processing configuration: {}", processingProperties);
        
        // Parse command line arguments
        boolean runOnce = false;
        boolean showThreads = false;
        boolean debugMode = false;
        
        for (String arg : args) {
            switch (arg) {
                case "--run-once":
                    runOnce = true;
                    log.info("Running in single-execution mode");
                    break;
                case "--show-threads":
                    showThreads = true;
                    log.info("Will display thread information");
                    break;
                case "--debug":
                    debugMode = true;
                    log.info("Debug mode enabled");
                    break;
            }
        }
        
        if (showThreads) {
            dataProcessingService.printThreadDump();
        }
        
        if (runOnce) {
            // Run processing once and exit
            log.info("Executing single data processing run...");
            dataProcessingService.processData();
            log.info("Single run completed. Total processed: {}", 
                     dataProcessingService.getTotalProcessedRecords());
        } else {
            // Continue running with scheduled tasks
            log.info("Application will continue running with scheduled tasks...");
            log.info("Use Ctrl+C to stop the application");
            log.info("Available arguments: --run-once, --show-threads, --debug");
        }
        log.info("=================================");
    }
}
```

## src/main/resources/application.yml
```yaml
# Application Configuration
app:
  processing:
    enabled: true
    batch-size: 150
    schedule-rate: 10000  # 10 seconds in milliseconds
  database:
    url: jdbc:h2:mem:standalone
    username: admin
    password: secret
    max-connections: 20

# Spring Configuration
spring:
  application:
    name: standalone-app
  main:
    web-application-type: none
    banner-mode: console
  profiles:
    active: default

# Logging Configuration
logging:
  level:
    com.example.standalone: INFO
    com.example.standalone.service: DEBUG
    org.springframework.boot: WARN
    org.springframework.scheduling: INFO
    org.springframework.context: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/standalone-app.log
    max-size: 10MB
    max-history: 30

# Management and Monitoring
management:
  endpoints:
    enabled-by-default: false
  endpoint:
    health:
      enabled: true
    info:
      enabled: true

---
# Development Profile
spring:
  config:
    activate:
      on-profile: dev
  
app:
  processing:
    schedule-rate: 5000  # 5 seconds for development

logging:
  level:
    com.example.standalone: DEBUG
    org.springframework: DEBUG

---
# Production Profile
spring:
  config:
    activate:
      on-profile: prod

app:
  processing:
    batch-size: 500
    schedule-rate: 30000  # 30 seconds for production

logging:
  level:
    com.example.standalone: INFO
    org.springframework: WARN
  file:
    name: /var/log/standalone-app/app.log
```

## src/main/resources/logback-spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/standalone-app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>logs/standalone-app.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- Async Appender for better performance -->
    <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="FILE"/>
        <queueSize>256</queueSize>
        <discardingThreshold>0</discardingThreshold>
    </appender>
    
    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ASYNC_FILE"/>
    </root>
    
    <!-- Application specific loggers -->
    <logger name="com.example.standalone" level="INFO" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ASYNC_FILE"/>
    </logger>
    
    <!-- Profile-specific configurations -->
    <springProfile name="dev">
        <logger name="com.example.standalone" level="DEBUG"/>
    </springProfile>
    
    <springProfile name="prod">
        <logger name="com.example.standalone" level="INFO"/>
        <logger name="org.springframework" level="WARN"/>
    </springProfile>
</configuration>
```

## scripts/start-app.sh
```bash
#!/bin/bash

# Spring Boot Standalone Application Launcher Script
# Usage: ./start-app.sh [options]

set -e

# Configuration
APP_NAME="standalone-app"
JAR_FILE="../target/${APP_NAME}-1.0.0.jar"
PID_FILE="../${APP_NAME}.pid"
LOG_DIR="../logs"
JAVA_OPTS="-Xms256m -Xmx1024m -XX:+UseG1GC"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

stop_application() {
    if [ ! -f "$PID_FILE" ]; then
        log_warn "PID file not found. Application may not be running."
        return 1
    fi
    
    PID=$(cat "$PID_FILE")
    
    if ! ps -p "$PID" > /dev/null 2>&1; then
        log_warn "Process with PID $PID is not running."
        rm -f "$PID_FILE"
        return 1
    fi
    
    log_info "Stopping $APP_NAME with PID: $PID"
    
    # Try graceful shutdown first
    kill -TERM "$PID"
    
    # Wait for graceful shutdown
    local count=0
    while ps -p "$PID" > /dev/null 2>&1 && [ $count -lt 30 ]; do
        sleep 1
        count=$((count + 1))
        echo -n "."
    done
    echo ""
    
    # Check if process is still running
    if ps -p "$PID" > /dev/null 2>&1; then
        log_warn "Graceful shutdown failed. Forcing shutdown..."
        kill -KILL "$PID"
        sleep 2
    fi
    
    # Verify shutdown
    if ps -p "$PID" > /dev/null 2>&1; then
        log_error "Failed to stop application with PID: $PID"
        return 1
    else
        log_info "Application stopped successfully"
        rm -f "$PID_FILE"
        return 0
    fi
}

# Main execution
main() {
    log_info "=== Spring Boot Standalone Application Stop Script ==="
    stop_application
}

main "$@"
    echo -e "${RED}[ERROR]${NC} $1"
}

check_java() {
    if ! command -v java &> /dev/null; then
        log_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
    log_info "Using Java version: $JAVA_VERSION"
}

check_jar() {
    if [ ! -f "$JAR_FILE" ]; then
        log_error "JAR file not found: $JAR_FILE"
        log_info "Please run 'mvn clean package' first"
        exit 1
    fi
}

create_directories() {
    mkdir -p "$LOG_DIR"
    log_info "Created log directory: $LOG_DIR"
}

check_if_running() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p "$PID" > /dev/null 2>&1; then
            log_warn "Application is already running with PID: $PID"
            return 0
        else
            log_warn "Stale PID file found. Removing..."
            rm -f "$PID_FILE"
        fi
    fi
    return 1
}

start_application() {
    local profile=""
    local extra_args=""
    
    # Parse arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            --profile=*)
                profile="${1#*=}"
                shift
                ;;
            --run-once)
                extra_args="$extra_args --run-once"
                shift
                ;;
            --show-threads)
                extra_args="$extra_args --show-threads"
                shift
                ;;
            --debug)
                extra_args="$extra_args --debug"
                JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
                shift
                ;;
            *)
                extra_args="$extra_args $1"
                shift
                ;;
        esac
    done
    
    # Set profile
    if [ -n "$profile" ]; then
        JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=$profile"
        log_info "Using profile: $profile"
    fi
    
    log_info "Starting $APP_NAME..."
    log_info "JAR file: $JAR_FILE"
    log_info "Java options: $JAVA_OPTS"
    log_info "Extra arguments: $extra_args"
    
    # Start application
    nohup java $JAVA_OPTS -jar "$JAR_FILE" $extra_args > "$LOG_DIR/console.log" 2>&1 &
    
    # Save PID
    echo $! > "$PID_FILE"
    PID=$(cat "$PID_FILE")
    
    log_info "Application started with PID: $PID"
    log_info "Console output: $LOG_DIR/console.log"
    log_info "Application logs: $LOG_DIR/standalone-app.log"
    
    # Wait a moment and check if it's still running
    sleep 3
    if ps -p "$PID" > /dev/null 2>&1; then
        log_info "Application is running successfully!"
    else
        log_error "Application failed to start. Check logs for details."
        rm -f "$PID_FILE"
        exit 1
    fi
}

show_usage() {
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --profile=PROFILE    Set Spring profile (dev, prod)"
    echo "  --run-once          Run once and exit"
    echo "  --show-threads      Show thread information"
    echo "  --debug             Enable debug mode with remote debugging"
    echo ""
    echo "Examples:"
    echo "  $0                          # Start with default profile"
    echo "  $0 --profile=dev            # Start with dev profile"
    echo "  $0 --run-once               # Run once and exit"
    echo "  $0 --debug --profile=dev    # Start in debug mode with dev profile"
}

# Main execution
main() {
    log_info "=== Spring Boot Standalone Application Launcher ==="
    
    if [[ $# -eq 1 && ($1 == "-h" || $1 == "--help") ]]; then
        show_usage
        exit 0
    fi
    
    check_java
    check_jar
    create_directories
    
    if check_if_running; then
        exit 1
    fi
    
    start_application "$@"
}

# Run main function with all arguments
main "$@"
```

## scripts/start-batch.sh
```bash
#!/bin/bash

# Batch Processing Mode Launcher
# Runs the application once and exits

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Starting application in batch mode..."
"$SCRIPT_DIR/start-app.sh" --run-once --profile=prod "$@"
```

## scripts/start-debug.sh
```bash
#!/bin/bash

# Debug Mode Launcher
# Starts application with remote debugging enabled

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Starting application in debug mode..."
echo "Remote debugging will be available on port 5005"
"$SCRIPT_DIR/start-app.sh" --debug --profile=dev --show-threads "$@"
```

## scripts/stop-app.sh
```bash
#!/bin/bash

# Spring Boot Standalone Application Stop Script

set -e

# Configuration
APP_NAME="standalone-app"
PID_FILE="../${APP_NAME}.pid"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
````

Perfect! I've created a complete, production-ready Spring Boot standalone application project. Here's what you get:

## 🎯 **Complete Project Structure**

### **Core Application Files:**
- **Main Application** - Comprehensive startup with system info logging
- **Data Processing Service** - Scheduled tasks with thread monitoring
- **Configuration Management** - Type-safe properties with validation
- **Startup Runner** - Command line argument processing

### **Shell Scripts for Operations:**
- **`start-app.sh`** - Main launcher with profile support
- **`start-batch.sh`** - Single-run batch processing
- **`start-debug.sh`** - Debug mode with remote debugging
- **`stop-app.sh`** - Graceful shutdown with fallback

### **Build & Deployment:**
- **`Makefile`** - All common operations (build, run, test, deploy)
- **`pom.xml`** - Maven configuration for Java 21 + Spring Boot 3.5.0
- **Configuration files** - YAML with dev/prod profiles
- **Logging setup** - Professional Logback configuration

## 🚀 **Quick Start:**

```bash
# Clone project and start
git clone <your-repo>
cd standalone-app

# Build and run (one command!)
make run

# Or run specific modes
make run-dev        # Development mode
make run-prod       # Production mode  
make run-batch      # Single execution
make run-debug      # Debug mode
```

## 🔧 **Key Features:**

### **Runtime Monitoring:**
- Process ID and thread tracking
- CPU usage per processing cycle
- JVM memory and system information
- Thread dumps on demand

### **Flexible Execution:**
```bash
# Different ways to run
./start-app.sh --profile=prod
./start-app.sh --run-once --show-threads
./start-app.sh --debug --profile=dev

# PropertiesLauncher support
java -Dloader.main=com.example.standalone.StandaloneApplication -jar app.jar
```

### **Production Features:**
- Graceful shutdown handling
- Log rotation and archiving
- Profile-based configuration
- Systemd service support
- Docker deployment ready

## 📊 **Runtime Output Example:**
```
2025-08-09 14:30:15.123 [main] INFO  c.e.s.StandaloneApplication - Process ID (PID): 12345
2025-08-09 14:30:15.124 [main] INFO  c.e.s.StandaloneApplication - JVM Version: OpenJDK 64-Bit Server VM 21.0.1
2025-08-09 14:30:25.456 [scheduling-1] INFO  c.e.s.s.DataProcessingService - ✅ Processed 150 records. Total: 150
```

This is a complete, enterprise-ready template that you can immediately use for any standalone Spring Boot application! 🎉





---
Powered by [Claude Exporter](https://www.claudexporter.com)