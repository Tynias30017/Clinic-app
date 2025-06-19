package org.example.clinic.service;

import org.example.clinic.model.Appointment;
import org.example.clinic.model.DoctorAvailability;
import org.example.clinic.model.User;
import org.example.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class AppointmentServiceTest {
    private final AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
    private final DoctorAvailabilityService doctorAvailabilityService = Mockito.mock(DoctorAvailabilityService.class);
    private final AppointmentService appointmentService = new AppointmentService(appointmentRepository, doctorAvailabilityService);

    @Test
    void shouldSaveAppointment() {
        User doctor = new User();
        doctor.setId(1L);
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setDate(LocalDateTime.of(2025, 6, 20, 10, 0));
        DoctorAvailability availability = DoctorAvailability.builder()
                .doctor(doctor)
                .availableFrom(LocalDateTime.of(2025, 6, 20, 9, 0))
                .availableTo(LocalDateTime.of(2025, 6, 20, 17, 0))
                .build();
        Mockito.when(doctorAvailabilityService.getDoctorAvailability(doctor)).thenReturn(List.of(availability));
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
