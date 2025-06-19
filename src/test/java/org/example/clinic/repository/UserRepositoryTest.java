package org.example.clinic.repository;

import org.example.clinic.model.User;
import org.example.clinic.model.User.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByUsername() {
        // Arrange
        User user = User.builder()
                .username("test_user")
                .password("password")
                .role(Role.PATIENT)
                .build();

        // Act
        userRepository.save(user);
        Optional<User> result = userRepository.findByUsername("test_user");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("test_user");
        assertThat(result.get().getRole()).isEqualTo(Role.PATIENT);
    }
}