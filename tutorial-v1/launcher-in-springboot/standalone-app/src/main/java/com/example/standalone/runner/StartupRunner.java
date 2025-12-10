package com.example.standalone.runner;

import com.example.standalone.service.DataProcessingService;
import com.example.standalone.config.ApplicationConfig.DatabaseProperties;
import com.example.standalone.config.ApplicationConfig.ProcessingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;

@Order(1) // Execution order if multiple CommandLineRunners exist
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    private DataProcessingService dataProcessingService;
    private DatabaseProperties databaseProperties;
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