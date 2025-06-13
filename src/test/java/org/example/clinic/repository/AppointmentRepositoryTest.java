package org.example.clinic.repository;

import org.example.clinic.model.Appointment;
import org.example.clinic.model.User;
import org.example.clinic.model.Appointment.Status;
import org.example.clinic.model.User.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindAppointments() {
        // Arrange
        User patient = userRepository.save(
                User.builder()
                        .username("patient")
                        .password("password")
                        .role(Role.USER)
                        .build()
        );

        User doctor = userRepository.save(
                User.builder()
                        .username("doctor")
                        .password("password")
                        .role(Role.ADMIN)
                        .build()
        );

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .date(LocalDateTime.now())
                .status(Status.PENDING)
                .build();

        // Act
        appointmentRepository.save(appointment);
        List<Appointment> result = appointmentRepository.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatient().getUsername()).isEqualTo("patient");
        assertThat(result.get(0).getDoctor().getUsername()).isEqualTo("doctor");
    }
}