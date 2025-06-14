package com.example.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJdbcRepositories(basePackages = "com.example.service.repository")
@EnableTransactionManagement
public class ServiceConfig {
}