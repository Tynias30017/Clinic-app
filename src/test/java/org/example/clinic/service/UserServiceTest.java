package org.example.clinic.service;

import org.example.clinic.model.User;
import org.example.clinic.model.User.Role;
import org.example.clinic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void shouldRegisterUser() {
        // Arrange
        User user = User.builder()
                .username("test_user")
                .password("password")
                .build();

        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.register(user);

        // Assert
        assertThat(result.getUsername()).isEqualTo("test_user");
        assertThat(passwordEncoder.matches("password", result.getPassword())).isTrue();
        assertThat(result.getRole()).isEqualTo(Role.PATIENT);
        Mockito.verify(userRepository).save(Mockito.argThat(savedUser ->
            savedUser.getRole() == Role.PATIENT && passwordEncoder.matches("password", savedUser.getPassword())
        ));
    }

    @Test
    void shouldFindUserByUsername() {
        // Arrange
        User user = User.builder().username("test_user").build();
        Mockito.when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByUsername("test_user");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("test_user");
    }

    @Test
    void shouldReturnAllUsers() {
        // Arrange
        User user1 = User.builder().username("user1").build();
        User user2 = User.builder().username("user2").build();
        List<User> users = Arrays.asList(user1, user2);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).hasSize(2).contains(user1, user2);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        Mockito.when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        // Act & Assert
        try {
            userService.loadUserByUsername("notfound");
        } catch (UsernameNotFoundException ex) {
            assertThat(ex.getMessage()).contains("User not found with username: notfound");
        }
    }
}