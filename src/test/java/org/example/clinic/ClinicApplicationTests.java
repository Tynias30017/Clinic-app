package org.example.clinic;

import org.example.clinic.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class ClinicApplicationTests {
    @Test
    void contextLoads() {
    }
}