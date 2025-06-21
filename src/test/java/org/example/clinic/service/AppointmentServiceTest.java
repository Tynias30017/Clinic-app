package org.example.clinic.service;

import org.example.clinic.model.Appointment;
import org.example.clinic.model.DoctorAvailability;
import org.example.clinic.model.User;
import org.example.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorAvailabilityService doctorAvailabilityService;
    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_whenDoctorAvailable() {
        Appointment appointment = new Appointment();
        User doctor = new User();
        appointment.setDoctor(doctor);
        appointment.setDate(LocalDateTime.of(2025, 6, 22, 10, 0));
        DoctorAvailability availability = new DoctorAvailability();
        availability.setAvailableFrom(LocalDateTime.of(2025, 6, 22, 9, 0));
        availability.setAvailableTo(LocalDateTime.of(2025, 6, 22, 12, 0));
        when(doctorAvailabilityService.getDoctorAvailability(doctor)).thenReturn(Arrays.asList(availability));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        Appointment result = appointmentService.save(appointment);
        assertEquals(appointment, result);
    }

    @Test
    void testSave_whenDoctorNotAvailable() {
        Appointment appointment = new Appointment();
        User doctor = new User();
        appointment.setDoctor(doctor);
        appointment.setDate(LocalDateTime.of(2025, 6, 22, 10, 0));
        when(doctorAvailabilityService.getDoctorAvailability(doctor)).thenReturn(List.of());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> appointmentService.save(appointment));
        assertEquals("Lekarz nie jest dostępny w wybranym terminie", exception.getReason());
    }

    @Test
    void testFindAll() {
        List<Appointment> appointments = Arrays.asList(new Appointment(), new Appointment());
        when(appointmentRepository.findAll()).thenReturn(appointments);
        List<Appointment> result = appointmentService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testCancelById() {
        doNothing().when(appointmentRepository).deleteById(1L);
        assertDoesNotThrow(() -> appointmentService.cancelById(1L));
        verify(appointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSave_whenDateEqualsAvailableFrom() {
        Appointment appointment = new Appointment();
        User doctor = new User();
        appointment.setDoctor(doctor);
        // Data równa availableFrom
        appointment.setDate(LocalDateTime.of(2025, 6, 22, 9, 0));
        DoctorAvailability availability = new DoctorAvailability();
        availability.setAvailableFrom(LocalDateTime.of(2025, 6, 22, 9, 0));
        availability.setAvailableTo(LocalDateTime.of(2025, 6, 22, 12, 0));
        when(doctorAvailabilityService.getDoctorAvailability(doctor)).thenReturn(List.of(availability));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        Appointment result = appointmentService.save(appointment);
        assertEquals(appointment, result);
    }

    @Test
    void testSave_whenDateEqualsAvailableTo() {
        Appointment appointment = new Appointment();
        User doctor = new User();
        appointment.setDoctor(doctor);
        // Data równa availableTo
        appointment.setDate(LocalDateTime.of(2025, 6, 22, 12, 0));
        DoctorAvailability availability = new DoctorAvailability();
        availability.setAvailableFrom(LocalDateTime.of(2025, 6, 22, 9, 0));
        availability.setAvailableTo(LocalDateTime.of(2025, 6, 22, 12, 0));
        when(doctorAvailabilityService.getDoctorAvailability(doctor)).thenReturn(List.of(availability));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        Appointment result = appointmentService.save(appointment);
        assertEquals(appointment, result);
    }
}
