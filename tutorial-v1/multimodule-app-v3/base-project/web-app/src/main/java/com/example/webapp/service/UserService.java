package com.example.webapp.service;

import com.example.common.ValidationUtils;
import com.example.data.model.User;
import com.example.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(String name, String email, int age) {
        logger.info("Creating user: name={}, email={}, age={}", name, email, age);
        
        ValidationUtils.requireNonNull(name, "Name is required");
        ValidationUtils.requireNonNull(email, "Email is required");
        
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        
        User user = new User(name, email, age);
        User savedUser = userRepository.save(user);
        
        logger.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }
    
    public Optional<User> getUserById(Long id) {
        logger.debug("Getting user by ID: {}", id);
        ValidationUtils.requireNonNull(id, "User ID is required");
        
        return userRepository.findById(id);
    }
    
    public List<User> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll();
    }
    
    public User updateUser(User user) {
        logger.info("Updating user: {}", user);
        ValidationUtils.requireNonNull(user, "User is required");
        ValidationUtils.requireNonNull(user.getId(), "User ID is required for update");
        
        // Verify user exists
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully: {}", updatedUser);
        return updatedUser;
    }
    
    public boolean deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        ValidationUtils.requireNonNull(id, "User ID is required");
        
        boolean deleted = userRepository.deleteById(id);
        if (deleted) {
            logger.info("User deleted successfully with ID: {}", id);
        } else {
            logger.warn("No user found to delete with ID: {}", id);
        }
        
        return deleted;
    }
    
    public long getUserCount() {
        return getAllUsers().size();
    }
}
