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
import static org.mockito.ArgumentMatchers.*;
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
    void getUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.getUserById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findById(999L);
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

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        // Given
        UserDto updateDto = UserDto.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .department("HR")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .department("HR")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("john.updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.updateUser(1L, updateDto);

        // Then
        assertThat(result.getName()).isEqualTo("John Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        assertThat(result.getDepartment()).isEqualTo("HR");
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(999L, testUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found with id: 999");

        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found with id: 999");

        verify(userRepository).existsById(999L);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getUsersByDepartment_ShouldReturnUsersInDepartment() {
        // Given
        when(userRepository.findByDepartment("IT")).thenReturn(Arrays.asList(testUser));

        // When
        List<UserDto> result = userService.getUsersByDepartment("IT");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartment()).isEqualTo("IT");
        verify(userRepository).findByDepartment("IT");
    }

    @Test
    void searchUsersByName_ShouldReturnMatchingUsers() {
        // Given
        when(userRepository.findByNameContaining("John")).thenReturn(Arrays.asList(testUser));

        // When
        List<UserDto> result = userService.searchUsersByName("John");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("John");
        verify(userRepository).findByNameContaining("John");
    }

    @Test
    void getUserByEmail_WhenEmailExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        // When
        Optional<UserDto> result = userService.getUserByEmail("john.doe@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void getUserCount_ShouldReturnTotalUserCount() {
        // Given
        when(userRepository.count()).thenReturn(5L);

        // When
        long result = userService.getUserCount();

        // Then
        assertThat(result).isEqualTo(5L);
        verify(userRepository).count();
    }

    @Test
    void existsById_WhenUserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = userService.existsById(1L);

        // Then
        assertThat(result).isTrue();
        verify(userRepository).existsById(1L);
    }

    @Test
    void existsById_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = userService.existsById(999L);

        // Then
        assertThat(result).isFalse();
        verify(userRepository).existsById(999L);
    }
}