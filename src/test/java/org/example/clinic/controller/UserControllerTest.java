package org.example.clinic.controller;

import org.example.clinic.model.User;
import org.example.clinic.service.UserService;
import org.example.clinic.dto.UserRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        UserRegisterRequest request = new UserRegisterRequest();
        User user = new User();
        when(userService.register(request)).thenReturn(user);
        User result = userController.register(request);
        assertEquals(user, result);
    }

    @Test
    void testLogin() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("pass");
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        String result = userController.login(user);
        assertTrue(result.contains("Login successful: test"));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.getAllUsers()).thenReturn(users);
        List<User> result = userController.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserById_found() {
        User user = new User();
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        User result = userController.getUserById(1L);
        assertEquals(user, result);
    }

    @Test
    void testGetUserById_notFound() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> userController.getUserById(2L));
        assertTrue(exception.getMessage().contains("User not found with id: 2"));
    }
}

