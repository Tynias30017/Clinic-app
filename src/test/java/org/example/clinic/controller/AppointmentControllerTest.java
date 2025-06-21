package org.example.clinic.controller;

import org.example.clinic.model.Appointment;
import org.example.clinic.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {
    @Mock
    private AppointmentService appointmentService;
    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Appointment appointment = new Appointment();
        when(appointmentService.save(appointment)).thenReturn(appointment);
        Appointment result = appointmentController.save(appointment);
        assertEquals(appointment, result);
    }

    @Test
    void testFindAll() {
        List<Appointment> appointments = Arrays.asList(new Appointment(), new Appointment());
        when(appointmentService.findAll()).thenReturn(appointments);
        List<Appointment> result = appointmentController.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testCancel() {
        doNothing().when(appointmentService).cancelById(1L);
        assertDoesNotThrow(() -> appointmentController.cancel(1L));
        verify(appointmentService, times(1)).cancelById(1L);
    }
}

