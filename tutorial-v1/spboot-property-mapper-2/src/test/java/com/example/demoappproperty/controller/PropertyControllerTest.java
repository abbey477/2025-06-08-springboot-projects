package com.example.demoappproperty.controller;

import com.example.demoappproperty.config.AppProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppProperties appProperties;

    @MockBean
    private com.example.demoappproperty.service.PropertyDisplayService propertyDisplayService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetBasicProperties() throws Exception {
        // Mock the basic properties
        when(appProperties.getName()).thenReturn("demo-app-property");
        when(appProperties.getVersion()).thenReturn("1.0.0");
        when(appProperties.getDescription()).thenReturn("Demo application for property mapping");
        when(appProperties.isDebug()).thenReturn(true);

        MvcResult result = mockMvc.perform(get("/api/properties/basic"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("demo-app-property"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.description").value("Demo application for property mapping"))
                .andExpect(jsonPath("$.debug").value(true))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);
        
        assertThat(response).hasSize(4);
        assertThat(response.get("name")).isEqualTo("demo-app-property");
        assertThat(response.get("version")).isEqualTo("1.0.0");
        assertThat(response.get("description")).isEqualTo("Demo application for property mapping");
        assertThat(response.get("debug")).isEqualTo(true);
    }

    @Test
    void testGetFeatures() throws Exception {
        // Mock features
        AppProperties.Features mockFeatures = new AppProperties.Features();
        mockFeatures.setUserRegistration(true);
        mockFeatures.setEmailNotifications(false);
        mockFeatures.setAuditLogging(true);
        mockFeatures.setCacheEnabled(true);
        
        when(appProperties.getFeatures()).thenReturn(mockFeatures);

        mockMvc.perform(get("/api/properties/features"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userRegistration").value(true))
                .andExpect(jsonPath("$.emailNotifications").value(false))
                .andExpect(jsonPath("$.auditLogging").value(true))
                .andExpect(jsonPath("$.cacheEnabled").value(true));
    }

    @Test
    void testGetListProperties() throws Exception {
        // Mock list properties
        when(appProperties.getAllowedOrigins()).thenReturn(Arrays.asList(
            "http://localhost:3000", "https://myapp.com", "https://staging.myapp.com"
        ));
        when(appProperties.getAdminEmails()).thenReturn(Arrays.asList(
            "admin@example.com", "support@example.com", "manager@example.com"
        ));
        when(appProperties.getSupportedLanguages()).thenReturn(Arrays.asList(
            "en", "es", "fr", "de", "it"
        ));
        when(appProperties.getCorsAllowedHeaders()).thenReturn(Arrays.asList(
            "Content-Type", "Authorization", "X-Requested-With"
        ));

        MvcResult result = mockMvc.perform(get("/api/properties/lists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.allowedOrigins").isArray())
                .andExpect(jsonPath("$.allowedOrigins.length()").value(3))
                .andExpect(jsonPath("$.adminEmails").isArray())
                .andExpect(jsonPath("$.adminEmails.length()").value(3))
                .andExpect(jsonPath("$.supportedLanguages").isArray())
                .andExpect(jsonPath("$.supportedLanguages.length()").value(5))
                .andExpect(jsonPath("$.corsAllowedHeaders").isArray())
                .andExpect(jsonPath("$.corsAllowedHeaders.length()").value(3))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);
        
        assertThat(response).hasSize(4);
        assertThat(response.get("allowedOrigins")).asList().hasSize(3);
        assertThat(response.get("adminEmails")).asList().hasSize(3);
        assertThat(response.get("supportedLanguages")).asList().hasSize(5);
        assertThat(response.get("corsAllowedHeaders")).asList().hasSize(3);
    }

    @Test
    void testDisplayPropertiesInConsole() throws Exception {
        mockMvc.perform(get("/api/properties/display"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Properties have been displayed in the console. Check the application logs."));
    }

    @Test
    void testGetAllPropertiesWithMaskedSensitiveData() throws Exception {
        // Mock all required properties
        setupMockAppProperties();

        MvcResult result = mockMvc.perform(get("/api/properties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("demo-app-property"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.database.password").value("[HIDDEN]"))
                .andExpect(jsonPath("$.email.password").value("[HIDDEN]"))
                .andExpect(jsonPath("$.security.jwt.secret").value("[HIDDEN]"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(content, Map.class);
        
        // Verify that sensitive data is masked
        Map<String, Object> database = (Map<String, Object>) response.get("database");
        assertThat(database.get("password")).isEqualTo("[HIDDEN]");
        
        Map<String, Object> email = (Map<String, Object>) response.get("email");
        assertThat(email.get("password")).isEqualTo("[HIDDEN]");
        
        Map<String, Object> security = (Map<String, Object>) response.get("security");
        Map<String, Object> jwt = (Map<String, Object>) security.get("jwt");
        assertThat(jwt.get("secret")).isEqualTo("[HIDDEN]");
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
        server.setSsl(ssl);
        when(appProperties.getServer()).thenReturn(server);

        // Database properties
        AppProperties.Database database = new AppProperties.Database();
        database.setUrl("jdbc:mysql://localhost:3306/demoapp");
        database.setUsername("demo_user");
        database.setPassword("demo_pass");
        AppProperties.Database.Pool pool = new AppProperties.Database.Pool();
        database.setPool(pool);
        when(appProperties.getDatabase()).thenReturn(database);

        // Email properties
        AppProperties.Email email = new AppProperties.Email();
        email.setHost("smtp.gmail.com");
        email.setPort(587);
        email.setUsername("demo@example.com");
        email.setPassword("email_password");
        when(appProperties.getEmail()).thenReturn(email);

        // Features
        AppProperties.Features features = new AppProperties.Features();
        features.setUserRegistration(true);
        when(appProperties.getFeatures()).thenReturn(features);

        // API
        AppProperties.Api api = new AppProperties.Api();
        api.setTimeout(5000L);
        when(appProperties.getApi()).thenReturn(api);

        // External services
        Map<String, AppProperties.ExternalService> externalServices = new HashMap<>();
        AppProperties.ExternalService paymentService = new AppProperties.ExternalService();
        paymentService.setApiKey("payment_key");
        externalServices.put("payment", paymentService);
        when(appProperties.getExternalServices()).thenReturn(externalServices);

        // Lists
        when(appProperties.getAllowedOrigins()).thenReturn(Arrays.asList("http://localhost:3000"));
        when(appProperties.getAdminEmails()).thenReturn(Arrays.asList("admin@example.com"));
        when(appProperties.getSupportedLanguages()).thenReturn(Arrays.asList("en"));
        when(appProperties.getCorsAllowedHeaders()).thenReturn(Arrays.asList("Content-Type"));

        // Maps
        when(appProperties.getErrorMessages()).thenReturn(new HashMap<>());
        when(appProperties.getEnvironments()).thenReturn(new HashMap<>());

        // Security
        AppProperties.Security security = new AppProperties.Security();
        AppProperties.Security.Jwt jwt = new AppProperties.Security.Jwt();
        jwt.setSecret("secret");
        jwt.setExpiration(86400L);
        security.setJwt(jwt);
        AppProperties.Security.PasswordPolicy passwordPolicy = new AppProperties.Security.PasswordPolicy();
        security.setPasswordPolicy(passwordPolicy);
        when(appProperties.getSecurity()).thenReturn(security);

        // Monitoring
        AppProperties.Monitoring monitoring = new AppProperties.Monitoring();
        AppProperties.Monitoring.Alerts alerts = new AppProperties.Monitoring.Alerts();
        monitoring.setAlerts(alerts);
        when(appProperties.getMonitoring()).thenReturn(monitoring);
    }
}