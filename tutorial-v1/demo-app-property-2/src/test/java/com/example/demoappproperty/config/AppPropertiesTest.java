package com.example.demoappproperty.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Test
    void testBasicProperties() {
        assertThat(appProperties.getName()).isEqualTo("demo-app-property");
        assertThat(appProperties.getVersion()).isEqualTo("1.0.0");
        assertThat(appProperties.getDescription()).isEqualTo("Demo application for property mapping");
        assertThat(appProperties.isDebug()).isTrue();
    }

    @Test
    void testServerConfiguration() {
        AppProperties.Server server = appProperties.getServer();
        
        assertThat(server).isNotNull();
        assertThat(server.getHost()).isEqualTo("localhost");
        assertThat(server.getPort()).isEqualTo(8080);
        
        AppProperties.Server.Ssl ssl = server.getSsl();
        assertThat(ssl).isNotNull();
        assertThat(ssl.isEnabled()).isFalse();
        assertThat(ssl.getKeystore()).isEqualTo("/path/to/keystore");
        assertThat(ssl.getPassword()).isEqualTo("changeit");
    }

    @Test
    void testDatabaseConfiguration() {
        AppProperties.Database database = appProperties.getDatabase();
        
        assertThat(database).isNotNull();
        assertThat(database.getUrl()).isEqualTo("jdbc:mysql://localhost:3306/demoapp");
        assertThat(database.getUsername()).isEqualTo("demo_user");
        assertThat(database.getPassword()).isEqualTo("demo_pass");
        assertThat(database.getDriverClassName()).isEqualTo("com.mysql.cj.jdbc.Driver");
        
        AppProperties.Database.Pool pool = database.getPool();
        assertThat(pool).isNotNull();
        assertThat(pool.getMaxSize()).isEqualTo(20);
        assertThat(pool.getMinSize()).isEqualTo(5);
        assertThat(pool.getTimeout()).isEqualTo(30000L);
    }

    @Test
    void testEmailConfiguration() {
        AppProperties.Email email = appProperties.getEmail();
        
        assertThat(email).isNotNull();
        assertThat(email.getHost()).isEqualTo("smtp.gmail.com");
        assertThat(email.getPort()).isEqualTo(587);
        assertThat(email.getUsername()).isEqualTo("demo@example.com");
        assertThat(email.getPassword()).isEqualTo("email_password");
        assertThat(email.isAuth()).isTrue();
        assertThat(email.isStarttls()).isTrue();
    }

    @Test
    void testFeatureFlags() {
        AppProperties.Features features = appProperties.getFeatures();
        
        assertThat(features).isNotNull();
        assertThat(features.isUserRegistration()).isTrue();
        assertThat(features.isEmailNotifications()).isFalse();
        assertThat(features.isAuditLogging()).isTrue();
        assertThat(features.isCacheEnabled()).isTrue();
    }

    @Test
    void testApiConfiguration() {
        AppProperties.Api api = appProperties.getApi();
        
        assertThat(api).isNotNull();
        assertThat(api.getTimeout()).isEqualTo(5000L);
        assertThat(api.getRetryAttempts()).isEqualTo(3);
        assertThat(api.getBaseUrl()).isEqualTo("https://api.example.com");
    }

    @Test
    void testExternalServices() {
        Map<String, AppProperties.ExternalService> externalServices = appProperties.getExternalServices();
        
        assertThat(externalServices).isNotNull();
        assertThat(externalServices).hasSize(2);
        
        // Test payment service
        AppProperties.ExternalService paymentService = externalServices.get("payment");
        assertThat(paymentService).isNotNull();
        assertThat(paymentService.getUrl()).isEqualTo("https://payment.gateway.com");
        assertThat(paymentService.getApiKey()).isEqualTo("payment_api_key_123");
        assertThat(paymentService.getTimeout()).isEqualTo(10000L);
        
        // Test notification service
        AppProperties.ExternalService notificationService = externalServices.get("notification");
        assertThat(notificationService).isNotNull();
        assertThat(notificationService.getUrl()).isEqualTo("https://notification.service.com");
        assertThat(notificationService.getApiKey()).isEqualTo("notification_api_key_456");
        assertThat(notificationService.getTimeout()).isEqualTo(5000L);
    }

    @Test
    void testListProperties() {
        List<String> allowedOrigins = appProperties.getAllowedOrigins();
        assertThat(allowedOrigins).isNotNull();
        assertThat(allowedOrigins).hasSize(3);
        assertThat(allowedOrigins).containsExactly(
            "http://localhost:3000", 
            "https://myapp.com", 
            "https://staging.myapp.com"
        );

        List<String> adminEmails = appProperties.getAdminEmails();
        assertThat(adminEmails).isNotNull();
        assertThat(adminEmails).hasSize(3);
        assertThat(adminEmails).containsExactly(
            "admin@example.com",
            "support@example.com", 
            "manager@example.com"
        );

        List<String> supportedLanguages = appProperties.getSupportedLanguages();
        assertThat(supportedLanguages).isNotNull();
        assertThat(supportedLanguages).hasSize(5);
        assertThat(supportedLanguages).containsExactly("en", "es", "fr", "de", "it");

        List<String> corsHeaders = appProperties.getCorsAllowedHeaders();
        assertThat(corsHeaders).isNotNull();
        assertThat(corsHeaders).hasSize(3);
        assertThat(corsHeaders).containsExactly(
            "Content-Type",
            "Authorization", 
            "X-Requested-With"
        );
    }

    @Test
    void testErrorMessages() {
        Map<String, Map<String, String>> errorMessages = appProperties.getErrorMessages();
        
        assertThat(errorMessages).isNotNull();
        assertThat(errorMessages).hasSize(3);
        
        // Test validation error messages
        Map<String, String> validationMessages = errorMessages.get("validation");
        assertThat(validationMessages).isNotNull();
        assertThat(validationMessages).hasSize(3);
        assertThat(validationMessages.get("required")).isEqualTo("This field is required");
        assertThat(validationMessages.get("email")).isEqualTo("Please enter a valid email address");
        assertThat(validationMessages.get("password")).isEqualTo("Password must be at least 8 characters");
        
        // Test auth error messages
        Map<String, String> authMessages = errorMessages.get("auth");
        assertThat(authMessages).isNotNull();
        assertThat(authMessages).hasSize(2);
        assertThat(authMessages.get("invalid-credentials")).isEqualTo("Invalid username or password");
        assertThat(authMessages.get("account-locked")).isEqualTo("Account has been locked");
        
        // Test general error messages
        Map<String, String> generalMessages = errorMessages.get("general");
        assertThat(generalMessages).isNotNull();
        assertThat(generalMessages).hasSize(1);
        assertThat(generalMessages.get("server-error")).isEqualTo("Internal server error occurred");
    }

    @Test
    void testEnvironmentConfigurations() {
        Map<String, AppProperties.Environment> environments = appProperties.getEnvironments();
        
        assertThat(environments).isNotNull();
        assertThat(environments).hasSize(3);
        
        // Test development environment
        AppProperties.Environment development = environments.get("development");
        assertThat(development).isNotNull();
        assertThat(development.getLogLevel()).isEqualTo("DEBUG");
        assertThat(development.isShowSql()).isTrue();
        assertThat(development.isCacheEnabled()).isFalse();
        
        // Test production environment
        AppProperties.Environment production = environments.get("production");
        assertThat(production).isNotNull();
        assertThat(production.getLogLevel()).isEqualTo("INFO");
        assertThat(production.isShowSql()).isFalse();
        assertThat(production.isCacheEnabled()).isTrue();
        
        // Test staging environment
        AppProperties.Environment staging = environments.get("staging");
        assertThat(staging).isNotNull();
        assertThat(staging.getLogLevel()).isEqualTo("WARN");
        assertThat(staging.isShowSql()).isTrue();
        assertThat(staging.isCacheEnabled()).isTrue();
    }

    @Test
    void testSecurityConfiguration() {
        AppProperties.Security security = appProperties.getSecurity();
        
        assertThat(security).isNotNull();
        
        // Test JWT configuration
        AppProperties.Security.Jwt jwt = security.getJwt();
        assertThat(jwt).isNotNull();
        assertThat(jwt.getSecret()).isEqualTo("mySecretKey123456789");
        assertThat(jwt.getExpiration()).isEqualTo(86400L);
        assertThat(jwt.getRefreshExpiration()).isEqualTo(604800L);
        
        // Test password policy
        AppProperties.Security.PasswordPolicy passwordPolicy = security.getPasswordPolicy();
        assertThat(passwordPolicy).isNotNull();
        assertThat(passwordPolicy.getMinLength()).isEqualTo(8);
        assertThat(passwordPolicy.isRequireUppercase()).isTrue();
        assertThat(passwordPolicy.isRequireNumbers()).isTrue();
        assertThat(passwordPolicy.isRequireSpecialChars()).isTrue();
    }

    @Test
    void testMonitoringConfiguration() {
        AppProperties.Monitoring monitoring = appProperties.getMonitoring();
        
        assertThat(monitoring).isNotNull();
        assertThat(monitoring.getHealthCheckInterval()).isEqualTo(30);
        assertThat(monitoring.isMetricsEnabled()).isTrue();
        
        AppProperties.Monitoring.Alerts alerts = monitoring.getAlerts();
        assertThat(alerts).isNotNull();
        assertThat(alerts.getCpuThreshold()).isEqualTo(80);
        assertThat(alerts.getMemoryThreshold()).isEqualTo(85);
        assertThat(alerts.getDiskThreshold()).isEqualTo(90);
    }

    @Test
    void testAllRequiredPropertiesAreNotNull() {
        // Test that all main objects are properly initialized
        assertThat(appProperties.getServer()).isNotNull();
        assertThat(appProperties.getDatabase()).isNotNull();
        assertThat(appProperties.getEmail()).isNotNull();
        assertThat(appProperties.getFeatures()).isNotNull();
        assertThat(appProperties.getApi()).isNotNull();
        assertThat(appProperties.getSecurity()).isNotNull();
        assertThat(appProperties.getMonitoring()).isNotNull();
        
        // Test that nested objects are initialized
        assertThat(appProperties.getServer().getSsl()).isNotNull();
        assertThat(appProperties.getDatabase().getPool()).isNotNull();
        assertThat(appProperties.getSecurity().getJwt()).isNotNull();
        assertThat(appProperties.getSecurity().getPasswordPolicy()).isNotNull();
        assertThat(appProperties.getMonitoring().getAlerts()).isNotNull();
    }

    @Test
    void testPropertyOverrides() {
        // This test verifies that properties are actually being loaded from the test file
        // The test properties should override the main application.properties
        assertThat(appProperties.getName()).isEqualTo("demo-app-property");
        assertThat(appProperties.isDebug()).isTrue();
    }
}