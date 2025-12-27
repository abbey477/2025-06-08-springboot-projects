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

    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Updating user with id: {}", id);

        return userRepository.findById(id)
                .map(existingUser -> {
                    // Check if email is being changed and if new email already exists
                    if (!existingUser.getEmail().equals(userDto.getEmail()) &&
                            userRepository.existsByEmail(userDto.getEmail())) {
                        throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
                    }

                    User updatedUser = User.builder()
                            .id(existingUser.getId())
                            .name(userDto.getName())
                            .email(userDto.getEmail())
                            .department(userDto.getDepartment())
                            .build();

                    User savedUser = userRepository.save(updatedUser);
                    return toDto(savedUser);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
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

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public long getUserCount() {
        log.info("Counting total users");
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    // Private helper methods for DTO conversion
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