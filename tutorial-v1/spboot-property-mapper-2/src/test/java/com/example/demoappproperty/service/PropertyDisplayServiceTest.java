package com.example.demoappproperty.service;

import com.example.demoappproperty.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class PropertyDisplayServiceTest {

    @Mock
    private AppProperties appProperties;

    private PropertyDisplayService propertyDisplayService;

    @BeforeEach
    void setUp() {
        propertyDisplayService = new PropertyDisplayService(appProperties);
        setupMockAppProperties();
    }

    @Test
    void testDisplayAllProperties(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        
        // Verify main sections are displayed
        assertThat(capturedOutput).contains("=== DEMO APP PROPERTY MAPPING DISPLAY ===");
        assertThat(capturedOutput).contains("--- BASIC APPLICATION PROPERTIES ---");
        assertThat(capturedOutput).contains("--- SERVER CONFIGURATION ---");
        assertThat(capturedOutput).contains("--- DATABASE CONFIGURATION ---");
        assertThat(capturedOutput).contains("--- EMAIL CONFIGURATION ---");
        assertThat(capturedOutput).contains("--- FEATURE FLAGS ---");
        assertThat(capturedOutput).contains("--- API CONFIGURATION ---");
        assertThat(capturedOutput).contains("--- EXTERNAL SERVICES ---");
        assertThat(capturedOutput).contains("--- LIST PROPERTIES ---");
        assertThat(capturedOutput).contains("--- ERROR MESSAGES (NESTED MAPS) ---");
        assertThat(capturedOutput).contains("--- ENVIRONMENT CONFIGURATIONS ---");
        assertThat(capturedOutput).contains("--- SECURITY CONFIGURATION ---");
        assertThat(capturedOutput).contains("--- MONITORING CONFIGURATION ---");
        assertThat(capturedOutput).contains("=== END OF PROPERTY MAPPING DISPLAY ===");

        // Verify specific property values are displayed
        assertThat(capturedOutput).contains("App Name: demo-app-property");
        assertThat(capturedOutput).contains("App Version: 1.0.0");
        assertThat(capturedOutput).contains("Debug Mode: true");
        assertThat(capturedOutput).contains("Server Host: localhost");
        assertThat(capturedOutput).contains("Server Port: 8080");
        assertThat(capturedOutput).contains("Database URL: jdbc:mysql://localhost:3306/demoapp");
        
        // Verify sensitive data is hidden
        assertThat(capturedOutput).contains("[HIDDEN]");
        assertThat(capturedOutput).doesNotContain("demo_pass");
        assertThat(capturedOutput).doesNotContain("email_password");
        assertThat(capturedOutput).doesNotContain("mySecretKey123456789");
    }

    @Test
    void testRunMethod(CapturedOutput output) {
        // When
        propertyDisplayService.run();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("=== DEMO APP PROPERTY MAPPING DISPLAY ===");
        assertThat(capturedOutput).contains("=== END OF PROPERTY MAPPING DISPLAY ===");
    }

    @Test
    void testDisplayBasicProperties(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("App Name: demo-app-property");
        assertThat(capturedOutput).contains("App Version: 1.0.0");
        assertThat(capturedOutput).contains("App Description: Demo application for property mapping");
        assertThat(capturedOutput).contains("Debug Mode: true");
    }

    @Test
    void testDisplayListProperties(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("Allowed Origins: [http://localhost:3000, https://myapp.com]");
        assertThat(capturedOutput).contains("Admin Emails: [admin@example.com, support@example.com]");
        assertThat(capturedOutput).contains("Supported Languages: [en, es, fr]");
    }

    @Test
    void testDisplayExternalServices(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("Service: payment");
        assertThat(capturedOutput).contains("URL: https://payment.gateway.com");
        assertThat(capturedOutput).contains("API Key: [HIDDEN]");
        assertThat(capturedOutput).contains("Timeout: 10000 ms");
    }

    @Test
    void testDisplayErrorMessages(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("Category: validation");
        assertThat(capturedOutput).contains("required: This field is required");
        assertThat(capturedOutput).contains("email: Please enter a valid email address");
    }

    @Test
    void testDisplayEnvironments(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("Environment: development");
        assertThat(capturedOutput).contains("Log Level: DEBUG");
        assertThat(capturedOutput).contains("Show SQL: true");
        assertThat(capturedOutput).contains("Cache Enabled: false");
    }

    @Test
    void testDisplaySecurityProperties(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("JWT Secret: [HIDDEN]");
        assertThat(capturedOutput).contains("JWT Expiration: 86400 seconds");
        assertThat(capturedOutput).contains("Password Min Length: 8");
        assertThat(capturedOutput).contains("Password Require Uppercase: true");
    }

    @Test
    void testDisplayMonitoringProperties(CapturedOutput output) {
        // When
        propertyDisplayService.displayAllProperties();

        // Then
        String capturedOutput = output.toString();
        assertThat(capturedOutput).contains("Health Check Interval: 30 seconds");
        assertThat(capturedOutput).contains("Metrics Enabled: true");
        assertThat(capturedOutput).contains("CPU Threshold: 80%");
        assertThat(capturedOutput).contains("Memory Threshold: 85%");
        assertThat(capturedOutput).contains("Disk Threshold: 90%");
    }

    private void setupMockAppProperties() {
        // Basic properties
        when(appProperties.getName()).thenReturn("demo-app-property");
        when(appProperties.getVersion()).thenReturn("1.0.0");
        when(appProperties.getDescription()).thenReturn("Demo application for property mapping");
        when(appProperties.isDebug()).thenReturn(true);

        // Server properties
        AppProperties.Server server = new AppProperties.Server();
        server.setHost("localhost");
        server.setPort(8080);
        AppProperties.Server.Ssl ssl = new AppProperties.Server.Ssl();
        ssl.setEnabled(false);
        ssl.setKeystore("/path/to/keystore");
        ssl.setPassword("changeit");
        server.setSsl(ssl);
        when(appProperties.getServer()).thenReturn(server);

        // Database properties
        AppProperties.Database database = new AppProperties.Database();
        database.setUrl("jdbc:mysql://localhost:3306/demoapp");
        database.setUsername("demo_user");
        database.setPassword("demo_pass");
        database.setDriverClassName("com.mysql.cj.jdbc.Driver");
        AppProperties.Database.Pool pool = new AppProperties.Database.Pool();
        pool.setMaxSize(20);
        pool.setMinSize(5);
        pool.setTimeout(30000L);
        database.setPool(pool);
        when(appProperties.getDatabase()).thenReturn(database);

        // Email properties
        AppProperties.Email email = new AppProperties.Email();
        email.setHost("smtp.gmail.com");
        email.setPort(587);
        email.setUsername("demo@example.com");
        email.setPassword("email_password");
        email.setAuth(true);
        email.setStarttls(true);
        when(appProperties.getEmail()).thenReturn(email);

        // Features
        AppProperties.Features features = new AppProperties.Features();
        features.setUserRegistration(true);
        features.setEmailNotifications(false);
        features.setAuditLogging(true);
        features.setCacheEnabled(true);
        when(appProperties.getFeatures()).thenReturn(features);

        // API
        AppProperties.Api api = new AppProperties.Api();
        api.setTimeout(5000L);
        api.setRetryAttempts(3);
        api.setBaseUrl("https://api.example.com");
        when(appProperties.getApi()).thenReturn(api);

        // External services
        Map<String, AppProperties.ExternalService> externalServices = new HashMap<>();
        AppProperties.ExternalService paymentService = new AppProperties.ExternalService();
        paymentService.setUrl("https://payment.gateway.com");
        paymentService.setApiKey("payment_api_key_123");
        paymentService.setTimeout(10000L);
        externalServices.put("payment", paymentService);
        when(appProperties.getExternalServices()).thenReturn(externalServices);

        // Lists
        when(appProperties.getAllowedOrigins()).thenReturn(Arrays.asList(
            "http://localhost:3000", "https://myapp.com"
        ));
        when(appProperties.getAdminEmails()).thenReturn(Arrays.asList(
            "admin@example.com", "support@example.com"
        ));
        when(appProperties.getSupportedLanguages()).thenReturn(Arrays.asList(
            "en", "es", "fr"
        ));
        when(appProperties.getCorsAllowedHeaders()).thenReturn(Arrays.asList(
            "Content-Type", "Authorization"
        ));

        // Error messages
        Map<String, Map<String, String>> errorMessages = new HashMap<>();
        Map<String, String> validationMessages = new HashMap<>();
        validationMessages.put("required", "This field is required");
        validationMessages.put("email", "Please enter a valid email address");
        errorMessages.put("validation", validationMessages);
        when(appProperties.getErrorMessages()).thenReturn(errorMessages);

        // Environments
        Map<String, AppProperties.Environment> environments = new HashMap<>();
        AppProperties.Environment development = new AppProperties.Environment();
        development.setLogLevel("DEBUG");
        development.setShowSql(true);
        development.setCacheEnabled(false);
        environments.put("development", development);
        when(appProperties.getEnvironments()).thenReturn(environments);

        // Security
        AppProperties.Security security = new AppProperties.Security();
        AppProperties.Security.Jwt jwt = new AppProperties.Security.Jwt();
        jwt.setSecret("mySecretKey123456789");
        jwt.setExpiration(86400L);
        jwt.setRefreshExpiration(604800L);
        security.setJwt(jwt);
        AppProperties.Security.PasswordPolicy passwordPolicy = new AppProperties.Security.PasswordPolicy();
        passwordPolicy.setMinLength(8);
        passwordPolicy.setRequireUppercase(true);
        passwordPolicy.setRequireNumbers(true);
        passwordPolicy.setRequireSpecialChars(true);
        security.setPasswordPolicy(passwordPolicy);
        when(appProperties.getSecurity()).thenReturn(security);

        // Monitoring
        AppProperties.Monitoring monitoring = new AppProperties.Monitoring();
        monitoring.setHealthCheckInterval(30);
        monitoring.setMetricsEnabled(true);
        AppProperties.Monitoring.Alerts alerts = new AppProperties.Monitoring.Alerts();
        alerts.setCpuThreshold(80);
        alerts.setMemoryThreshold(85);
        alerts.setDiskThreshold(90);
        monitoring.setAlerts(alerts);
        when(appProperties.getMonitoring()).thenReturn(monitoring);
    }
}