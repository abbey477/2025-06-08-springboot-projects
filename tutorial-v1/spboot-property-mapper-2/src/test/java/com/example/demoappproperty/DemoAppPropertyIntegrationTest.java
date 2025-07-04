package com.example.demoappproperty;

import com.example.demoappproperty.config.AppProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class DemoAppPropertyIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppProperties appProperties;

    @Test
    void contextLoads() {
        // Verify that the Spring context loads successfully
        assertThat(appProperties).isNotNull();
    }

    @Test
    void testApplicationPropertiesAreInjected() {
        // Verify that properties are correctly injected
        assertThat(appProperties.getName()).isEqualTo("demo-app-property");
        assertThat(appProperties.getVersion()).isEqualTo("1.0.0");
        assertThat(appProperties.isDebug()).isTrue();
        
        // Verify nested properties
        assertThat(appProperties.getServer().getHost()).isEqualTo("localhost");
        assertThat(appProperties.getDatabase().getUrl()).isEqualTo("jdbc:mysql://localhost:3306/demoapp");
        assertThat(appProperties.getEmail().getHost()).isEqualTo("smtp.gmail.com");
    }

    @Test
    void testBasicPropertiesEndpoint() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/properties/basic", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("name")).isEqualTo("demo-app-property");
        assertThat(response.getBody().get("version")).isEqualTo("1.0.0");
        assertThat(response.getBody().get("debug")).isEqualTo(true);
    }

    @Test
    void testFeaturesEndpoint() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/properties/features", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("userRegistration")).isEqualTo(true);
        assertThat(response.getBody().get("emailNotifications")).isEqualTo(false);
        assertThat(response.getBody().get("auditLogging")).isEqualTo(true);
        assertThat(response.getBody().get("cacheEnabled")).isEqualTo(true);
    }

    @Test
    void testListPropertiesEndpoint() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/properties/lists", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        List<String> allowedOrigins = (List<String>) response.getBody().get("allowedOrigins");
        assertThat(allowedOrigins).hasSize(3);
        assertThat(allowedOrigins).contains("http://localhost:3000", "https://myapp.com", "https://staging.myapp.com");
        
        List<String> adminEmails = (List<String>) response.getBody().get("adminEmails");
        assertThat(adminEmails).hasSize(3);
        assertThat(adminEmails).contains("admin@example.com", "support@example.com", "manager@example.com");
    }

    @Test
    void testAllPropertiesEndpoint() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/properties", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        // Verify basic properties
        assertThat(response.getBody().get("name")).isEqualTo("demo-app-property");
        assertThat(response.getBody().get("version")).isEqualTo("1.0.0");
        
        // Verify nested properties exist
        assertThat(response.getBody().get("server")).isNotNull();
        assertThat(response.getBody().get("database")).isNotNull();
        assertThat(response.getBody().get("email")).isNotNull();
        
        // Verify sensitive data is masked
        Map<String, Object> database = (Map<String, Object>) response.getBody().get("database");
        assertThat(database.get("password")).isEqualTo("[HIDDEN]");
        
        Map<String, Object> email = (Map<String, Object>) response.getBody().get("email");
        assertThat(email.get("password")).isEqualTo("[HIDDEN]");
    }

    @Test
    void testDisplayEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/properties/display", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Properties have been displayed in the console. Check the application logs.");
    }

    @Test
    void testComplexPropertyMappings() {
        // Test external services mapping
        assertThat(appProperties.getExternalServices()).isNotNull();
        assertThat(appProperties.getExternalServices()).hasSize(2);
        
        AppProperties.ExternalService paymentService = appProperties.getExternalServices().get("payment");
        assertThat(paymentService).isNotNull();
        assertThat(paymentService.getUrl()).isEqualTo("https://payment.gateway.com");
        assertThat(paymentService.getApiKey()).isEqualTo("payment_api_key_123");
        
        // Test error messages mapping
        assertThat(appProperties.getErrorMessages()).isNotNull();
        assertThat(appProperties.getErrorMessages().get("validation")).isNotNull();
        assertThat(appProperties.getErrorMessages().get("validation").get("required"))
            .isEqualTo("This field is required");
        
        // Test environments mapping
        assertThat(appProperties.getEnvironments()).isNotNull();
        AppProperties.Environment development = appProperties.getEnvironments().get("development");
        assertThat(development).isNotNull();
        assertThat(development.getLogLevel()).isEqualTo("DEBUG");
        assertThat(development.isShowSql()).isTrue();
        assertThat(development.isCacheEnabled()).isFalse();
    }

    @Test
    void testNestedPropertyStructures() {
        // Test deeply nested structures
        AppProperties.Security.Jwt jwt = appProperties.getSecurity().getJwt();
        assertThat(jwt).isNotNull();
        assertThat(jwt.getSecret()).isEqualTo("mySecretKey123456789");
        assertThat(jwt.getExpiration()).isEqualTo(86400L);
        
        AppProperties.Security.PasswordPolicy passwordPolicy = appProperties.getSecurity().getPasswordPolicy();
        assertThat(passwordPolicy).isNotNull();
        assertThat(passwordPolicy.getMinLength()).isEqualTo(8);
        assertThat(passwordPolicy.isRequireUppercase()).isTrue();
        
        AppProperties.Monitoring.Alerts alerts = appProperties.getMonitoring().getAlerts();
        assertThat(alerts).isNotNull();
        assertThat(alerts.getCpuThreshold()).isEqualTo(80);
        assertThat(alerts.getMemoryThreshold()).isEqualTo(85);
        assertThat(alerts.getDiskThreshold()).isEqualTo(90);
    }

    @Test
    void testListAndMapPropertyTypes() {
        // Test list properties
        assertThat(appProperties.getAllowedOrigins()).isNotEmpty();
        assertThat(appProperties.getAdminEmails()).isNotEmpty();
        assertThat(appProperties.getSupportedLanguages()).isNotEmpty();
        assertThat(appProperties.getCorsAllowedHeaders()).isNotEmpty();
        
        // Test map properties
        assertThat(appProperties.getExternalServices()).isNotEmpty();
        assertThat(appProperties.getErrorMessages()).isNotEmpty();
        assertThat(appProperties.getEnvironments()).isNotEmpty();
        
        // Verify specific list contents
        assertThat(appProperties.getSupportedLanguages()).containsExactly("en", "es", "fr", "de", "it");
        assertThat(appProperties.getCorsAllowedHeaders()).contains("Content-Type", "Authorization", "X-Requested-With");
    }

    @Test
    void testPropertyValidation() {
        // Test that all required nested objects are initialized
        assertThat(appProperties.getServer()).isNotNull();
        assertThat(appProperties.getServer().getSsl()).isNotNull();
        assertThat(appProperties.getDatabase()).isNotNull();
        assertThat(appProperties.getDatabase().getPool()).isNotNull();
        assertThat(appProperties.getEmail()).isNotNull();
        assertThat(appProperties.getFeatures()).isNotNull();
        assertThat(appProperties.getApi()).isNotNull();
        assertThat(appProperties.getSecurity()).isNotNull();
        assertThat(appProperties.getSecurity().getJwt()).isNotNull();
        assertThat(appProperties.getSecurity().getPasswordPolicy()).isNotNull();
        assertThat(appProperties.getMonitoring()).isNotNull();
        assertThat(appProperties.getMonitoring().getAlerts()).isNotNull();
        
        // Test that numeric properties are correctly mapped
        assertThat(appProperties.getServer().getPort()).isEqualTo(8080);
        assertThat(appProperties.getEmail().getPort()).isEqualTo(587);
        assertThat(appProperties.getApi().getTimeout()).isEqualTo(5000L);
        assertThat(appProperties.getApi().getRetryAttempts()).isEqualTo(3);
        
        // Test boolean properties
        assertThat(appProperties.isDebug()).isTrue();
        assertThat(appProperties.getServer().getSsl().isEnabled()).isFalse();
        assertThat(appProperties.getEmail().isAuth()).isTrue();
        assertThat(appProperties.getFeatures().isUserRegistration()).isTrue();
        assertThat(appProperties.getFeatures().isEmailNotifications()).isFalse();
    }
}