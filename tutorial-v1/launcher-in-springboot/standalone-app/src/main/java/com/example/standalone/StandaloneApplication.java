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