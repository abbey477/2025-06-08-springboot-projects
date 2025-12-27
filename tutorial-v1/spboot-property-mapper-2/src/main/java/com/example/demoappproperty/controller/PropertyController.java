package com.example.demoappproperty.controller;

import com.example.demoappproperty.config.AppProperties;
import com.example.demoappproperty.service.PropertyDisplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {
    
    private final AppProperties appProperties;
    private final PropertyDisplayService propertyDisplayService;
    
    @GetMapping
    public Map<String, Object> getAllProperties() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", appProperties.getName());
        response.put("version", appProperties.getVersion());
        response.put("description", appProperties.getDescription());
        response.put("debug", appProperties.isDebug());
        response.put("server", appProperties.getServer());
        response.put("database", maskSensitiveData(appProperties.getDatabase()));
        response.put("email", maskSensitiveEmailData(appProperties.getEmail()));
        response.put("features", appProperties.getFeatures());
        response.put("api", appProperties.getApi());
        response.put("externalServices", maskExternalServicesData(appProperties.getExternalServices()));
        response.put("allowedOrigins", appProperties.getAllowedOrigins());
        response.put("adminEmails", appProperties.getAdminEmails());
        response.put("supportedLanguages", appProperties.getSupportedLanguages());
        response.put("corsAllowedHeaders", appProperties.getCorsAllowedHeaders());
        response.put("errorMessages", appProperties.getErrorMessages());
        response.put("environments", appProperties.getEnvironments());
        response.put("security", maskSecurityData(appProperties.getSecurity()));
        response.put("monitoring", appProperties.getMonitoring());
        
        return response;
    }
    
    @GetMapping("/basic")
    public Map<String, Object> getBasicProperties() {
        Map<String, Object> basic = new HashMap<>();
        basic.put("name", appProperties.getName());
        basic.put("version", appProperties.getVersion());
        basic.put("description", appProperties.getDescription());
        basic.put("debug", appProperties.isDebug());
        return basic;
    }
    
    @GetMapping("/features")
    public AppProperties.Features getFeatures() {
        return appProperties.getFeatures();
    }
    
    @GetMapping("/lists")
    public Map<String, Object> getListProperties() {
        Map<String, Object> lists = new HashMap<>();
        lists.put("allowedOrigins", appProperties.getAllowedOrigins());
        lists.put("adminEmails", appProperties.getAdminEmails());
        lists.put("supportedLanguages", appProperties.getSupportedLanguages());
        lists.put("corsAllowedHeaders", appProperties.getCorsAllowedHeaders());
        return lists;
    }
    
    @GetMapping("/display")
    public String displayPropertiesInConsole() {
        propertyDisplayService.displayAllProperties();
        return "Properties have been displayed in the console. Check the application logs.";
    }
    
    private AppProperties.Database maskSensitiveData(AppProperties.Database database) {
        AppProperties.Database masked = new AppProperties.Database();
        masked.setUrl(database.getUrl());
        masked.setUsername(database.getUsername());
        masked.setPassword("[HIDDEN]");
        masked.setDriverClassName(database.getDriverClassName());
        masked.setPool(database.getPool());
        return masked;
    }
    
    private AppProperties.Email maskSensitiveEmailData(AppProperties.Email email) {
        AppProperties.Email masked = new AppProperties.Email();
        masked.setHost(email.getHost());
        masked.setPort(email.getPort());
        masked.setUsername(email.getUsername());
        masked.setPassword("[HIDDEN]");
        masked.setAuth(email.isAuth());
        masked.setStarttls(email.isStarttls());
        return masked;
    }
    
    private Map<String, AppProperties.ExternalService> maskExternalServicesData(
            Map<String, AppProperties.ExternalService> services) {
        if (services == null) return null;
        
        Map<String, AppProperties.ExternalService> masked = new HashMap<>();
        services.forEach((key, service) -> {
            AppProperties.ExternalService maskedService = new AppProperties.ExternalService();
            maskedService.setUrl(service.getUrl());
            maskedService.setApiKey("[HIDDEN]");
            maskedService.setTimeout(service.getTimeout());
            masked.put(key, maskedService);
        });
        return masked;
    }
    
    private AppProperties.Security maskSecurityData(AppProperties.Security security) {
        AppProperties.Security masked = new AppProperties.Security();
        AppProperties.Security.Jwt maskedJwt = new AppProperties.Security.Jwt();
        maskedJwt.setSecret("[HIDDEN]");
        maskedJwt.setExpiration(security.getJwt().getExpiration());
        maskedJwt.setRefreshExpiration(security.getJwt().getRefreshExpiration());
        masked.setJwt(maskedJwt);
        masked.setPasswordPolicy(security.getPasswordPolicy());
        return masked;
    }
}