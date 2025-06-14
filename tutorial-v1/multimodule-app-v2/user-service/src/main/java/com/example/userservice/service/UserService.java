package com.example.userservice.service;

import com.example.common.model.User;
import com.example.common.util.StringUtils;
import com.example.common.util.ValidationUtils;
import com.example.common.exception.BusinessException;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        // Use shared validation utilities
        if (!ValidationUtils.isValidName(user.getName())) {
            throw new BusinessException("Invalid name: " + user.getName());
        }

        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            throw new BusinessException("Invalid email: " + user.getEmail());
        }

        // Use shared string utilities
        user.setName(StringUtils.capitalize(StringUtils.sanitize(user.getName())));
        user.setEmail(StringUtils.sanitize(user.getEmail()));

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("User with email " + user.getEmail() + " already exists");
        }

        User savedUser = userRepository.save(user);
        log.info("Created user: {}", savedUser);
        return savedUser;
    }

    public User updateUser(Long id, User userUpdate) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (StringUtils.isNotEmpty(userUpdate.getName())) {
                        if (!ValidationUtils.isValidName(userUpdate.getName())) {
                            throw new BusinessException("Invalid name: " + userUpdate.getName());
                        }
                        existingUser.setName(StringUtils.capitalize(StringUtils.sanitize(userUpdate.getName())));
                    }
                    if (StringUtils.isNotEmpty(userUpdate.getEmail())) {
                        if (!ValidationUtils.isValidEmail(userUpdate.getEmail())) {
                            throw new BusinessException("Invalid email: " + userUpdate.getEmail());
                        }
                        existingUser.setEmail(StringUtils.sanitize(userUpdate.getEmail()));
                    }
                    User updated = userRepository.save(existingUser);
                    log.info("Updated user: {}", updated);
                    return updated;
                })
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }
}