package com.example.webapp.service;

import com.example.data.model.User;
import com.example.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }
    
    @Test
    @DisplayName("Should create user successfully")
    void testCreateUser() {
        String name = "John Doe";
        String email = "john@example.com";
        int age = 30;
        
        User expectedUser = new User(name, email, age);
        expectedUser.setId(1L);
        
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        
        User result = userService.createUser(name, email, age);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(age, result.getAge());
        
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should reject invalid age")
    void testCreateUserInvalidAge() {
        assertThrows(IllegalArgumentException.class, 
                    () -> userService.createUser("John", "john@example.com", -1));
        
        assertThrows(IllegalArgumentException.class, 
                    () -> userService.createUser("John", "john@example.com", 200));
        
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should get user by ID")
    void testGetUserById() {
        Long userId = 1L;
        User expectedUser = new User("Jane", "jane@example.com", 25);
        expectedUser.setId(userId);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        
        Optional<User> result = userService.getUserById(userId);
        
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        
        verify(userRepository).findById(userId);
    }
    
    @Test
    @DisplayName("Should return empty optional for non-existent user")
    void testGetUserByIdNotFound() {
        Long userId = 999L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        Optional<User> result = userService.getUserById(userId);
        
        assertFalse(result.isPresent());
        
        verify(userRepository).findById(userId);
    }
    
    @Test
    @DisplayName("Should get all users")
    void testGetAllUsers() {
        List<User> expectedUsers = List.of(
            new User("User1", "user1@example.com", 20),
            new User("User2", "user2@example.com", 30)
        );
        
        when(userRepository.findAll()).thenReturn(expectedUsers);
        
        List<User> result = userService.getAllUsers();
        
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        
        verify(userRepository).findAll();
    }
    
    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUser() {
        User existingUser = new User("John", "john@example.com", 30);
        existingUser.setId(1L);
        
        User updatedUser = new User("John Doe", "john@example.com", 31);
        updatedUser.setId(1L);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        
        User result = userService.updateUser(updatedUser);
        
        assertEquals(updatedUser, result);
        
        verify(userRepository).findById(1L);
        verify(userRepository).save(updatedUser);
    }
    
    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void testUpdateNonExistentUser() {
        User user = new User("John", "john@example.com", 30);
        user.setId(999L);
        
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.updateUser(user)
        );
        
        assertEquals("User not found with ID: 999", exception.getMessage());
        
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() {
        Long userId = 1L;
        
        when(userRepository.deleteById(userId)).thenReturn(true);
        
        boolean result = userService.deleteUser(userId);
        
        assertTrue(result);
        
        verify(userRepository).deleteById(userId);
    }
    
    @Test
    @DisplayName("Should return false when deleting non-existent user")
    void testDeleteNonExistentUser() {
        Long userId = 999L;
        
        when(userRepository.deleteById(userId)).thenReturn(false);
        
        boolean result = userService.deleteUser(userId);
        
        assertFalse(result);
        
        verify(userRepository).deleteById(userId);
    }
    
    @Test
    @DisplayName("Should get user count")
    void testGetUserCount() {
        List<User> users = List.of(
            new User("User1", "user1@example.com", 20),
            new User("User2", "user2@example.com", 30),
            new User("User3", "user3@example.com", 40)
        );
        
        when(userRepository.findAll()).thenReturn(users);
        
        long count = userService.getUserCount();
        
        assertEquals(3, count);
        
        verify(userRepository).findAll();
    }
}
