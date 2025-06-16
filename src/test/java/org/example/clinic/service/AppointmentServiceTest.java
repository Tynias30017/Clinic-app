package org.example.clinic.service;

import org.example.clinic.model.Appointment;
import org.example.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class AppointmentServiceTest {
    private final AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
    private final AppointmentService appointmentService = new AppointmentService(appointmentRepository);

    @Test
    void shouldSaveAppointment() {
        Appointment appointment = new Appointment();
        Mockito.when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        Appointment result = appointmentService.save(appointment);
        assertThat(result).isNotNull();
        Mockito.verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldFindAllAppointments() {
        Appointment a1 = new Appointment();
        Appointment a2 = new Appointment();
        List<Appointment> list = Arrays.asList(a1, a2);
        Mockito.when(appointmentRepository.findAll()).thenReturn(list);
        List<Appointment> result = appointmentService.findAll();
        assertThat(result).hasSize(2).contains(a1, a2);
    }
}

