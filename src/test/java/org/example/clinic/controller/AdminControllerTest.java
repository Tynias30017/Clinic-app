package org.example.clinic.controller;

import org.example.clinic.model.Appointment;
import org.example.clinic.model.User;
import org.example.clinic.service.AppointmentService;
import org.example.clinic.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {
    @Mock
    private AppointmentService appointmentService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStats() {
        User doctor = new User();
        doctor.setRole(User.Role.DOCTOR);
        User patient = new User();
        patient.setRole(User.Role.PATIENT);
        List<User> users = Arrays.asList(doctor, patient);
        when(userService.getAllUsers()).thenReturn(users);
        when(appointmentService.findAll()).thenReturn(Arrays.asList(new Appointment(), new Appointment()));
        Map<String, Object> stats = adminController.getStats();
        assertEquals(2, stats.get("totalUsers"));
        assertEquals(1L, stats.get("totalDoctors"));
        assertEquals(1L, stats.get("totalPatients"));
        assertEquals(2, stats.get("totalAppointments"));
    }

    @Test
    void testGetAllAppointments() {
        List<Appointment> appointments = Arrays.asList(new Appointment(), new Appointment());
        when(appointmentService.findAll()).thenReturn(appointments);
        List<Appointment> result = adminController.getAllAppointments();
        assertEquals(2, result.size());
    }
}

