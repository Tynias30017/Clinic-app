package org.example.clinic.controller;

import org.example.clinic.model.User;
import org.example.clinic.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerLogicTest {
    @Autowired
    private MockMvc mockMvc;

    private UserService userService = mock(UserService.class);
    private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
        @Bean
        public AuthenticationManager authenticationManager() {
            return mock(AuthenticationManager.class);
        }
    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = User.builder().username("test").password("pass").build();
        when(userService.register(any(org.example.clinic.dto.UserRegisterRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test\",\"password\":\"pass\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldLoginUser() throws Exception {
        User user = User.builder().username("test").password("pass").build();
        Authentication auth = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test\",\"password\":\"pass\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful: test"));
    }
}
