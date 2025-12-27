package com.example.standalone.service;

import com.example.standalone.config.ApplicationConfig.ProcessingProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class DataProcessingService {

    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);

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