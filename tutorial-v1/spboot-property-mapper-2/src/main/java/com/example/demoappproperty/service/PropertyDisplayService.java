package com.example.demoappproperty.service;

import com.example.demoappproperty.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyDisplayService implements CommandLineRunner {
    
    private final AppProperties appProperties;
    
    @Override
    public void run(String... args) {
        displayAllProperties();
    }
    
    public void displayAllProperties() {
        log.info("=== DEMO APP PROPERTY MAPPING DISPLAY ===");
        
        displayBasicProperties();
        displayServerProperties();
        displayDatabaseProperties();
        displayEmailProperties();
        displayFeatureFlags();
        displayApiProperties();
        displayExternalServices();
        displayListProperties();
        displayErrorMessages();
        displayEnvironments();
        displaySecurityProperties();
        displayMonitoringProperties();
        
        log.info("=== END OF PROPERTY MAPPING DISPLAY ===");
    }
    
    private void displayBasicProperties() {
        log.info("\n--- BASIC APPLICATION PROPERTIES ---");
        log.info("App Name: {}", appProperties.getName());
        log.info("App Version: {}", appProperties.getVersion());
        log.info("App Description: {}", appProperties.getDescription());
        log.info("Debug Mode: {}", appProperties.isDebug());
    }
    
    private void displayServerProperties() {
        log.info("\n--- SERVER CONFIGURATION ---");
        AppProperties.Server server = appProperties.getServer();
        log.info("Server Host: {}", server.getHost());
        log.info("Server Port: {}", server.getPort());
        log.info("SSL Enabled: {}", server.getSsl().isEnabled());
        log.info("SSL Keystore: {}", server.getSsl().getKeystore());
        log.info("SSL Password: [HIDDEN]");
    }
    
    private void displayDatabaseProperties() {
        log.info("\n--- DATABASE CONFIGURATION ---");
        AppProperties.Database db = appProperties.getDatabase();
        log.info("Database URL: {}", db.getUrl());
        log.info("Database Username: {}", db.getUsername());
        log.info("Database Password: [HIDDEN]");
        log.info("Database Driver: {}", db.getDriverClassName());
        log.info("Connection Pool Max Size: {}", db.getPool().getMaxSize());
        log.info("Connection Pool Min Size: {}", db.getPool().getMinSize());
        log.info("Connection Pool Timeout: {}", db.getPool().getTimeout());
    }
    
    private void displayEmailProperties() {
        log.info("\n--- EMAIL CONFIGURATION ---");
        AppProperties.Email email = appProperties.getEmail();
        log.info("Email Host: {}", email.getHost());
        log.info("Email Port: {}", email.getPort());
        log.info("Email Username: {}", email.getUsername());
        log.info("Email Password: [HIDDEN]");
        log.info("Email Auth: {}", email.isAuth());
        log.info("Email STARTTLS: {}", email.isStarttls());
    }
    
    private void displayFeatureFlags() {
        log.info("\n--- FEATURE FLAGS ---");
        AppProperties.Features features = appProperties.getFeatures();
        log.info("User Registration: {}", features.isUserRegistration());
        log.info("Email Notifications: {}", features.isEmailNotifications());
        log.info("Audit Logging: {}", features.isAuditLogging());
        log.info("Cache Enabled: {}", features.isCacheEnabled());
    }
    
    private void displayApiProperties() {
        log.info("\n--- API CONFIGURATION ---");
        AppProperties.Api api = appProperties.getApi();
        log.info("API Timeout: {} ms", api.getTimeout());
        log.info("API Retry Attempts: {}", api.getRetryAttempts());
        log.info("API Base URL: {}", api.getBaseUrl());
    }
    
    private void displayExternalServices() {
        log.info("\n--- EXTERNAL SERVICES ---");
        if (appProperties.getExternalServices() != null) {
            appProperties.getExternalServices().forEach((serviceName, service) -> {
                log.info("Service: {}", serviceName);
                log.info("  URL: {}", service.getUrl());
                log.info("  API Key: [HIDDEN]");
                log.info("  Timeout: {} ms", service.getTimeout());
            });
        }
    }
    
    private void displayListProperties() {
        log.info("\n--- LIST PROPERTIES ---");
        log.info("Allowed Origins: {}", appProperties.getAllowedOrigins());
        log.info("Admin Emails: {}", appProperties.getAdminEmails());
        log.info("Supported Languages: {}", appProperties.getSupportedLanguages());
        log.info("CORS Allowed Headers: {}", appProperties.getCorsAllowedHeaders());
    }
    
    private void displayErrorMessages() {
        log.info("\n--- ERROR MESSAGES (NESTED MAPS) ---");
        if (appProperties.getErrorMessages() != null) {
            appProperties.getErrorMessages().forEach((category, messages) -> {
                log.info("Category: {}", category);
                messages.forEach((key, value) -> log.info("  {}: {}", key, value));
            });
        }
    }
    
    private void displayEnvironments() {
        log.info("\n--- ENVIRONMENT CONFIGURATIONS ---");
        if (appProperties.getEnvironments() != null) {
            appProperties.getEnvironments().forEach((envName, env) -> {
                log.info("Environment: {}", envName);
                log.info("  Log Level: {}", env.getLogLevel());
                log.info("  Show SQL: {}", env.isShowSql());
                log.info("  Cache Enabled: {}", env.isCacheEnabled());
            });
        }
    }
    
    private void displaySecurityProperties() {
        log.info("\n--- SECURITY CONFIGURATION ---");
        AppProperties.Security security = appProperties.getSecurity();
        log.info("JWT Secret: [HIDDEN]");
        log.info("JWT Expiration: {} seconds", security.getJwt().getExpiration());
        log.info("JWT Refresh Expiration: {} seconds", security.getJwt().getRefreshExpiration());
        log.info("Password Min Length: {}", security.getPasswordPolicy().getMinLength());
        log.info("Password Require Uppercase: {}", security.getPasswordPolicy().isRequireUppercase());
        log.info("Password Require Numbers: {}", security.getPasswordPolicy().isRequireNumbers());
        log.info("Password Require Special Chars: {}", security.getPasswordPolicy().isRequireSpecialChars());
    }
    
    private void displayMonitoringProperties() {
        log.info("\n--- MONITORING CONFIGURATION ---");
        AppProperties.Monitoring monitoring = appProperties.getMonitoring();
        log.info("Health Check Interval: {} seconds", monitoring.getHealthCheckInterval());
        log.info("Metrics Enabled: {}", monitoring.isMetricsEnabled());
        log.info("CPU Threshold: {}%", monitoring.getAlerts().getCpuThreshold());
        log.info("Memory Threshold: {}%", monitoring.getAlerts().getMemoryThreshold());
        log.info("Disk Threshold: {}%", monitoring.getAlerts().getDiskThreshold());
    }
}