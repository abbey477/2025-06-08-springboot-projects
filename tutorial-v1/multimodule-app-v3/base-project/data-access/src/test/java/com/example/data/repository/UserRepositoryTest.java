package com.example.data.repository;

import com.example.data.config.DatabaseConfig;
import com.example.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRepository Tests")
class UserRepositoryTest {
    
    private UserRepository userRepository;
    private DataSource dataSource;
    
    @BeforeEach
    void setUp() {
        dataSource = DatabaseConfig.createDataSource();
        userRepository = new UserRepository(dataSource);
    }
    
    @Test
    @DisplayName("Should save and retrieve user")
    void testSaveAndRetrieveUser() {
        User user = new User("Alice Smith", "alice@example.com", 25);
        User savedUser = userRepository.save(user);
        
        assertNotNull(savedUser.getId());
        assertEquals("Alice Smith", savedUser.getName());
        assertEquals("alice@example.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
        
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals(savedUser, retrievedUser.get());
    }
    
    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser() {
        User user = new User("Bob Jones", "bob@example.com", 35);
        User savedUser = userRepository.save(user);
        
        savedUser.setName("Robert Jones");
        savedUser.setAge(36);
        
        User updatedUser = userRepository.save(savedUser);
        assertEquals("Robert Jones", updatedUser.getName());
        assertEquals(36, updatedUser.getAge());
        
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("Robert Jones", retrievedUser.get().getName());
        assertEquals(36, retrievedUser.get().getAge());
    }
    
    @Test
    @DisplayName("Should find all users")
    void testFindAllUsers() {
        userRepository.save(new User("User1", "user1@example.com", 20));
        userRepository.save(new User("User2", "user2@example.com", 25));
        userRepository.save(new User("User3", "user3@example.com", 30));
        
        List<User> users = userRepository.findAll();
        assertEquals(3, users.size());
    }
    
    @Test
    @DisplayName("Should delete user by id")
    void testDeleteUser() {
        User user = new User("Charlie Brown", "charlie@example.com", 40);
        User savedUser = userRepository.save(user);
        
        assertTrue(userRepository.deleteById(savedUser.getId()));
        
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }
    
    @Test
    @DisplayName("Should return false when deleting non-existent user")
    void testDeleteNonExistentUser() {
        assertFalse(userRepository.deleteById(999L));
    }
    
    @Test
    @DisplayName("Should return empty optional for non-existent user")
    void testFindNonExistentUser() {
        Optional<User> user = userRepository.findById(999L);
        assertFalse(user.isPresent());
    }
}
