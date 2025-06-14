// web-module/src/main/java/com/example/web/WebApplication.java
package com.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.web", "com.example.service"})
public class WebApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}