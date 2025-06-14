package com.example.webservice.client;

import com.example.common.model.User;
import com.example.common.model.ApiResponse;
import com.example.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    public Optional<ApiResponse> getAllUsers() {
        try {
            ResponseEntity<ApiResponse> response = restTemplate.getForEntity(
                    userServiceUrl + "/api/users",
                    ApiResponse.class
            );
            log.info("Got response from user service: {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error calling user service: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<ApiResponse> createUser(User user) {
        try {
            // Use shared utilities for preprocessing
            if (StringUtils.isNotEmpty(user.getName())) {
                user.setName(StringUtils.sanitize(user.getName()));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> request = new HttpEntity<>(user, headers);

            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    userServiceUrl + "/api/users",
                    request,
                    ApiResponse.class
            );
            log.info("Created user via user service: {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<ApiResponse> getUserById(Long id) {
        try {
            ResponseEntity<ApiResponse> response = restTemplate.getForEntity(
                    userServiceUrl + "/api/users/" + id,
                    ApiResponse.class
            );
            log.info("Got user by id from user service: {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error getting user by id: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<ApiResponse> updateUser(Long id, User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> request = new HttpEntity<>(user, headers);

            restTemplate.put(userServiceUrl + "/api/users/" + id, request);

            // Get updated user
            return getUserById(id);
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        try {
            restTemplate.delete(userServiceUrl + "/api/users/" + id);
            log.info("Deleted user with id: {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return false;
        }
    }

    public Optional<ApiResponse> checkUserServiceHealth() {
        try {
            ResponseEntity<ApiResponse> response = restTemplate.getForEntity(
                    userServiceUrl + "/actuator/health",
                    ApiResponse.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("User service health check failed: {}", e.getMessage());
            return Optional.empty();
        }
    }
}