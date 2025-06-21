package org.example.clinic.controller;

import org.example.clinic.model.User;
import org.example.clinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import org.example.clinic.dto.UserRegisterRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Rejestracja nowego użytkownika (domyślnie pacjent)")
    @PostMapping("/register")
    public User register(@RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    @Operation(summary = "Logowanie użytkownika")
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "Login successful: " + user.getUsername();
    }

    @Operation(summary = "Pobierz wszystkich użytkowników")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Pobierz użytkownika po ID")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Operation(summary = "Usuń użytkownika po ID")
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @Operation(summary = "Aktualizuj dane użytkownika po ID")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}