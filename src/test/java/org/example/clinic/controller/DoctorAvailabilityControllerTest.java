package org.example.clinic.controller;

import org.example.clinic.model.DoctorAvailability;
import org.example.clinic.model.User;
import org.example.clinic.service.DoctorAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorAvailabilityControllerTest {
    @Mock
    private DoctorAvailabilityService doctorAvailabilityService;
    @InjectMocks
    private DoctorAvailabilityController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAvailability() {
        DoctorAvailability availability = new DoctorAvailability();
        when(doctorAvailabilityService.addAvailability(availability)).thenReturn(availability);
        DoctorAvailability result = controller.addAvailability(availability);
        assertEquals(availability, result);
    }

    @Test
    void testGetDoctorAvailability() {
        List<DoctorAvailability> list = Arrays.asList(new DoctorAvailability(), new DoctorAvailability());
        when(doctorAvailabilityService.getDoctorAvailability(any(User.class))).thenReturn(list);
        List<DoctorAvailability> result = controller.getDoctorAvailability(1L);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllAvailability() {
        List<DoctorAvailability> list = Arrays.asList(new DoctorAvailability());
        when(doctorAvailabilityService.getAllAvailability()).thenReturn(list);
        List<DoctorAvailability> result = controller.getAllAvailability();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateAvailability() {
        DoctorAvailability availability = new DoctorAvailability();
        when(doctorAvailabilityService.updateAvailability(1L, availability)).thenReturn(availability);
        DoctorAvailability result = controller.updateAvailability(1L, availability);
        assertEquals(availability, result);
    }

    @Test
    void testDeleteAvailability() {
        doNothing().when(doctorAvailabilityService).deleteAvailabilityById(1L);
        assertDoesNotThrow(() -> controller.deleteAvailability(1L));
        verify(doctorAvailabilityService, times(1)).deleteAvailabilityById(1L);
    }
}

