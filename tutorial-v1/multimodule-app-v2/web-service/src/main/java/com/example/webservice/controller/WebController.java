package com.example.webservice.controller;

import com.example.common.model.User;
import com.example.common.model.ApiResponse;
import com.example.common.util.ValidationUtils;
import com.example.webservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
public class WebController {

    private final UserServiceClient userServiceClient;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUsers() {
        log.info("Web layer: Getting all users");

        return userServiceClient.getAllUsers()
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        log.info("Web layer: Getting user by id: {}", id);

        return userServiceClient.getUserById(id)
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
        log.info("Web layer: Creating user with email: {}", user.getEmail());

        // Use shared validation from common library
        if (!ValidationUtils.isValidName(user.getName())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid name provided"));
        }

        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email provided"));
        }

        return userServiceClient.createUser(user)
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Web layer: Updating user with id: {}", id);

        // Basic validation
        if (user.getName() != null && !ValidationUtils.isValidName(user.getName())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid name provided"));
        }

        if (user.getEmail() != null && !ValidationUtils.isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email provided"));
        }

        return userServiceClient.updateUser(id, user)
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service unavailable")));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        log.info("Web layer: Deleting user with id: {}", id);

        boolean deleted = userServiceClient.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } else {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to delete user"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        log.info("Web layer: Health check");
        return ResponseEntity.ok(ApiResponse.success("Web service is running!"));
    }

    @GetMapping("/validate-email/{email}")
    public ResponseEntity<ApiResponse> validateEmail(@PathVariable String email) {
        log.info("Web layer: Validating email: {}", email);

        boolean isValid = ValidationUtils.isValidEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Email validation result", isValid));
    }

    @GetMapping("/validate-name/{name}")
    public ResponseEntity<ApiResponse> validateName(@PathVariable String name) {
        log.info("Web layer: Validating name: {}", name);

        boolean isValid = ValidationUtils.isValidName(name);
        return ResponseEntity.ok(ApiResponse.success("Name validation result", isValid));
    }

    @GetMapping("/user-service-status")
    public ResponseEntity<ApiResponse<ApiResponse>> checkUserServiceStatus() {
        log.info("Web layer: Checking user service status");

        return userServiceClient.checkUserServiceHealth()
                .map(response -> ResponseEntity.ok(ApiResponse.success("User service is healthy", response)))
                .orElse(ResponseEntity.status(500)
                        .body(ApiResponse.error("User service is down")));
    }
}