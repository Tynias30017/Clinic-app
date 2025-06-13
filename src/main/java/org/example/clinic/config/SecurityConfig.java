package org.example.clinic.config;

import org.example.clinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Wyłączenie CSRF
            .authorizeHttpRequests(auth -> { // Konfiguracja reguł autoryzacji
                auth.requestMatchers("/api/users/register", "/api/users/login").permitAll(); // Publiczne endpointy
                auth.requestMatchers(HttpMethod.GET, "/api/appointments/**").hasAnyRole("USER", "ADMIN");
                auth.requestMatchers(HttpMethod.POST, "/api/appointments/**").hasRole("ADMIN");
                auth.anyRequest().authenticated(); // Wymagaj uwierzytelnienia dla reszty
            })
            .httpBasic(Customizer.withDefaults()); // Nowa poprawna składnia HTTP Basic
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService::loadUserByUsername;
    }
}