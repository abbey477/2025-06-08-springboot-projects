package com.example.web.config;

import com.example.service.UserService;
import com.example.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        log.info("Initializing sample data...");

        try {
            // Create sample users
            UserDto user1 = UserDto.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .department("IT")
                    .build();

            UserDto user2 = UserDto.builder()
                    .name("Jane Smith")
                    .email("jane.smith@example.com")
                    .department("HR")
                    .build();

            UserDto user3 = UserDto.builder()
                    .name("Bob Johnson")
                    .email("bob.johnson@example.com")
                    .department("IT")
                    .build();

            UserDto user4 = UserDto.builder()
                    .name("Alice Brown")
                    .email("alice.brown@example.com")
                    .department("Finance")
                    .build();

            userService.createUser(user1);
            userService.createUser(user2);
            userService.createUser(user3);
            userService.createUser(user4);

            log.info("Sample data initialized successfully!");

        } catch (Exception e) {
            log.error("Error initializing sample data: {}", e.getMessage());
        }
    }
}