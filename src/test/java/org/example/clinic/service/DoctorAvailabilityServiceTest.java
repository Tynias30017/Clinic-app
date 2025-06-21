package org.example.clinic.service;

import org.example.clinic.model.DoctorAvailability;
import org.example.clinic.model.User;
import org.example.clinic.repository.DoctorAvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorAvailabilityServiceTest {
    @Mock
    private DoctorAvailabilityRepository repository;
    @InjectMocks
    private DoctorAvailabilityService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAvailability() {
        DoctorAvailability availability = new DoctorAvailability();
        when(repository.save(availability)).thenReturn(availability);
        DoctorAvailability result = service.addAvailability(availability);
        assertEquals(availability, result);
    }

    @Test
    void testGetDoctorAvailability() {
        User doctor = new User();
        List<DoctorAvailability> list = Arrays.asList(new DoctorAvailability());
        when(repository.findByDoctor(doctor)).thenReturn(list);
        List<DoctorAvailability> result = service.getDoctorAvailability(doctor);
        assertEquals(1, result.size());
    }

    @Test
    void testGetDoctorAvailability_emptyList() {
        User doctor = new User();
        when(repository.findByDoctor(doctor)).thenReturn(List.of());
        List<DoctorAvailability> result = service.getDoctorAvailability(doctor);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAvailability() {
        List<DoctorAvailability> list = Arrays.asList(new DoctorAvailability(), new DoctorAvailability());
        when(repository.findAll()).thenReturn(list);
        List<DoctorAvailability> result = service.getAllAvailability();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllAvailability_emptyList() {
        when(repository.findAll()).thenReturn(List.of());
        List<DoctorAvailability> result = service.getAllAvailability();
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateAvailability_found() {
        DoctorAvailability existing = new DoctorAvailability();
        existing.setAvailableFrom(LocalDateTime.now());
        existing.setAvailableTo(LocalDateTime.now().plusHours(1));
        DoctorAvailability updated = new DoctorAvailability();
        updated.setAvailableFrom(LocalDateTime.now().plusDays(1));
        updated.setAvailableTo(LocalDateTime.now().plusDays(1).plusHours(1));
        updated.setDoctor(new User());
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(DoctorAvailability.class))).thenReturn(updated);
        DoctorAvailability result = service.updateAvailability(1L, updated);
        assertEquals(updated.getAvailableFrom(), result.getAvailableFrom());
        assertEquals(updated.getAvailableTo(), result.getAvailableTo());
    }

    @Test
    void testUpdateAvailability_notFound() {
        Long id = 1L;
        DoctorAvailability updated = new DoctorAvailability();
        when(repository.findById(id)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateAvailability(id, updated));
        assertTrue(exception.getMessage().contains("Doctor availability not found with id: " + id));
    }

    @Test
    void testDeleteAvailabilityById() {
        doNothing().when(repository).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteAvailabilityById(1L));
        verify(repository, times(1)).deleteById(1L);
    }
}
