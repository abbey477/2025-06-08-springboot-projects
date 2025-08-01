package com.example.data.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Model Tests")
class UserTest {
    
    @Test
    @DisplayName("Should create user with valid data")
    void testCreateValidUser() {
        User user = new User("John Doe", "john@example.com", 30);
        
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(30, user.getAge());
        assertNull(user.getId());
    }
    
    @Test
    @DisplayName("Should reject null name")
    void testRejectNullName() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new User(null, "john@example.com", 30)
        );
        assertEquals("Name cannot be null", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should reject null email")
    void testRejectNullEmail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new User("John", null, 30)
        );
        assertEquals("Email cannot be null", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should reject invalid email")
    void testRejectInvalidEmail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new User("John", "invalid-email", 30)
        );
        assertEquals("Invalid email format", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        User user1 = new User("John", "john@example.com", 30);
        user1.setId(1L);
        
        User user2 = new User("John", "john@example.com", 30);
        user2.setId(1L);
        
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
