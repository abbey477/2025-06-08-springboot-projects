package com.example.demoappproperty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private String name;
    private String version;
    private String description;
    private boolean debug;
    
    // Nested server configuration
    private Server server = new Server();
    
    // Nested database configuration
    private Database database = new Database();
    
    // Nested email configuration
    private Email email = new Email();
    
    // Feature flags
    private Features features = new Features();
    
    // API configuration  
    private Api api = new Api();
    
    // External services configuration
    private Map<String, ExternalService> externalServices;
    
    // List properties
    private List<String> allowedOrigins;
    private List<String> adminEmails;
    private List<String> supportedLanguages;
    private List<String> corsAllowedHeaders;
    
    // Map properties for error messages
    private Map<String, Map<String, String>> errorMessages;
    
    // Environment-specific configurations
    private Map<String, Environment> environments;
    
    // Security configuration
    private Security security = new Security();
    
    // Monitoring configuration
    private Monitoring monitoring = new Monitoring();
    
    @Data
    public static class Server {
        private String host;
        private int port;
        private Ssl ssl = new Ssl();
        
        @Data
        public static class Ssl {
            private boolean enabled;
            private String keystore;
            private String password;
        }
    }
    
    @Data
    public static class Database {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private Pool pool = new Pool();
        
        @Data
        public static class Pool {
            private int maxSize;
            private int minSize;
            private long timeout;
        }
    }
    
    @Data
    public static class Email {
        private String host;
        private int port;
        private String username;
        private String password;
        private boolean auth;
        private boolean starttls;
    }
    
    @Data
    public static class Features {
        private boolean userRegistration;
        private boolean emailNotifications;
        private boolean auditLogging;
        private boolean cacheEnabled;
    }
    
    @Data
    public static class Api {
        private long timeout;
        private int retryAttempts;
        private String baseUrl;
    }
    
    @Data
    public static class ExternalService {
        private String url;
        private String apiKey;
        private long timeout;
    }
    
    @Data
    public static class Environment {
        private String logLevel;
        private boolean showSql;
        private boolean cacheEnabled;
    }
    
    @Data
    public static class Security {
        private Jwt jwt = new Jwt();
        private PasswordPolicy passwordPolicy = new PasswordPolicy();
        
        @Data
        public static class Jwt {
            private String secret;
            private long expiration;
            private long refreshExpiration;
        }
        
        @Data
        public static class PasswordPolicy {
            private int minLength;
            private boolean requireUppercase;
            private boolean requireNumbers;
            private boolean requireSpecialChars;
        }
    }
    
    @Data
    public static class Monitoring {
        private int healthCheckInterval;
        private boolean metricsEnabled;
        private Alerts alerts = new Alerts();
        
        @Data
        public static class Alerts {
            private int cpuThreshold;
            private int memoryThreshold;
            private int diskThreshold;
        }
    }
}