package com.example.web.controller;

import com.example.service.UserService;
import com.example.service.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    //@MockBean
    @MockitoBean
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