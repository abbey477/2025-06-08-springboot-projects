package com.example.standalone.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

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