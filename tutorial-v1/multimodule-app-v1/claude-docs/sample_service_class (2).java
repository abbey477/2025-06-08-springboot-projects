// service-module/src/main/java/com/example/service/model/User.java
package com.example.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Table("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column("name")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column("email")
    private String email;
    
    @Column("department")
    private String department;
}

// service-module/src/main/java/com/example/service/dto/UserDto.java
package com.example.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class UserDto {
    
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    private String department;
}

// service-module/src/main/java/com/example/service/UserService.java
package com.example.service;

import com.example.service.dto.UserDto;
import com.example.service.model.User;
import com.example.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(this::toDto);
    }
    
    public UserDto createUser(UserDto userDto) {
        log.info("Creating new user: {}", userDto.getName());
        
        // Check if email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }
        
        User user = toEntity(userDto);
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }
    
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByDepartment(String department) {
        log.info("Fetching users by department: {}", department);
        return userRepository.findByDepartment(department)
                .stream()
                .map(this::toDto)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByName(String name) {
        log.info("Searching users by name containing: {}", name);
        return userRepository.findByNameContaining(name)
                .stream()
                .map(this::toDto)
                .toList();
    }
    
    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .build();
    }
    
    private User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .build();
    }
}