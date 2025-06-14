// service-module/src/main/java/com/example/service/config/ServiceConfig.java
package com.example.service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.service.repository")
@EntityScan(basePackages = "com.example.service.model")
@EnableTransactionManagement
public class ServiceConfig {
}