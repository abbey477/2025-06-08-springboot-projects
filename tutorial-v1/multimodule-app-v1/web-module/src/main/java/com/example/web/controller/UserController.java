package com.example.web.controller;

import com.example.service.UserService;
import com.example.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("GET /api/users - Fetching all users");
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Fetching user by id", id);
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST /api/users - Creating new user: {}", userDto.getName());
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<UserDto>> getUsersByDepartment(@PathVariable String department) {
        log.info("GET /api/users/department/{} - Fetching users by department", department);
        List<UserDto> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsersByName(@RequestParam String name) {
        log.info("GET /api/users/search?name={} - Searching users by name", name);
        List<UserDto> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
}