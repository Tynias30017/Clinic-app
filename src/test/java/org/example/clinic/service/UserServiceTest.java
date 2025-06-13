package org.example.clinic.service;

import org.example.clinic.model.User;
import org.example.clinic.model.User.Role;
import org.example.clinic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        assertThat(result.getRole()).isEqualTo(Role.USER);
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
}