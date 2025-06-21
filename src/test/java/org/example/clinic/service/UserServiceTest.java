package org.example.clinic.service;

import org.example.clinic.model.User;
import org.example.clinic.repository.UserRepository;
import org.example.clinic.dto.UserRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_found() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpass");
        user.setRole(User.Role.PATIENT);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        UserDetails details = userService.loadUserByUsername("testuser");
        assertEquals("testuser", details.getUsername());
        assertEquals("encodedpass", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT")));
    }

    @Test
    void testLoadUserByUsername_notFound() {
        when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nouser"));
    }

    @Test
    void testRegister_withRole() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("user1");
        request.setPassword("pass1");
        request.setRole(User.Role.DOCTOR);
        User user = new User();
        user.setUsername("user1");
        user.setPassword("encoded");
        user.setRole(User.Role.DOCTOR);
        when(passwordEncoder.encode("pass1")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.register(request);
        assertEquals("user1", result.getUsername());
        assertEquals("encoded", result.getPassword());
        assertEquals(User.Role.DOCTOR, result.getRole());
    }

    @Test
    void testRegister_withoutRole() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("user2");
        request.setPassword("pass2");
        request.setRole(null);
        User user = new User();
        user.setUsername("user2");
        user.setPassword("encoded2");
        user.setRole(User.Role.PATIENT);
        when(passwordEncoder.encode("pass2")).thenReturn("encoded2");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.register(request);
        assertEquals("user2", result.getUsername());
        assertEquals("encoded2", result.getPassword());
        assertEquals(User.Role.PATIENT, result.getRole());
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByUsername("test");
        assertTrue(result.isPresent());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserById_found() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserById_notFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserById(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteById(1L);
        assertDoesNotThrow(() -> userService.deleteUserById(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUser_notFound() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        Optional<User> result = userService.updateUser(3L, new User());
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUser_updateAllFields() {
        User existing = new User();
        existing.setUsername("old");
        existing.setPassword("oldpass");
        existing.setRole(User.Role.PATIENT);
        User update = new User();
        update.setUsername("new");
        update.setPassword("newpass");
        update.setRole(User.Role.DOCTOR);
        when(userRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        Optional<User> result = userService.updateUser(4L, update);
        assertTrue(result.isPresent());
        assertEquals("new", result.get().getUsername());
        assertEquals("encoded", result.get().getPassword());
        assertEquals(User.Role.DOCTOR, result.get().getRole());
    }

    @Test
    void testUpdateUser_passwordNullOrEmpty() {
        User existing = new User();
        existing.setUsername("old");
        existing.setPassword("oldpass");
        existing.setRole(User.Role.PATIENT);
        User update = new User();
        update.setUsername("new");
        update.setPassword(null);
        update.setRole(User.Role.DOCTOR);
        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        Optional<User> result = userService.updateUser(5L, update);
        assertTrue(result.isPresent());
        assertEquals("new", result.get().getUsername());
        assertEquals("oldpass", result.get().getPassword());
        assertEquals(User.Role.DOCTOR, result.get().getRole());
        // Sprawdź także pusty string
        update.setPassword("");
        Optional<User> result2 = userService.updateUser(5L, update);
        assertTrue(result2.isPresent());
        assertEquals("oldpass", result2.get().getPassword());
    }
}
