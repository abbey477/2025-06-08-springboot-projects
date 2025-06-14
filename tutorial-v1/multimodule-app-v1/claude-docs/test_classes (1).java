// service-module/src/test/java/com/example/service/UserServiceTest.java
package com.example.service;

import com.example.service.dto.UserDto;
import com.example.service.model.User;
import com.example.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    private UserDto testUserDto;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .build();
        
        testUserDto = UserDto.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .build();
    }
    
    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));
        
        // When
        List<UserDto> result = userService.getAllUsers();
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        verify(userRepository).findAll();
    }
    
    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        // When
        Optional<UserDto> result = userService.getUserById(1L);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
        verify(userRepository).findById(1L);
    }
    
    @Test
    void createUser_WhenEmailDoesNotExist_ShouldCreateUser() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // When
        UserDto result = userService.createUser(testUserDto);
        
        // Then
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(userRepository).existsByEmail("john.doe@example.com");
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void createUser_WhenEmailExists_ShouldThrowException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> userService.createUser(testUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");
        
        verify(userRepository).existsByEmail("john.doe@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
}

// web-module/src/test/java/com/example/web/controller/UserControllerTest.java
package com.example.web.controller;

import com.example.service.UserService;
import com.example.service.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .build();
        
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDto));
        
        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }
    
    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .build();
        
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(userDto));
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
    
    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        when(userService.getUserById(anyLong())).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createUser_WithValidData_ShouldCreateUser() throws Exception {
        // Given
        UserDto inputDto = UserDto.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .department("HR")
                .build();
        
        UserDto responseDto = UserDto.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .department("HR")
                .build();
        
        when(userService.createUser(any(UserDto.class))).thenReturn(responseDto);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }
}