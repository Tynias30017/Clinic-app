package org.example.clinic.service;

import org.example.clinic.model.Appointment;
import org.example.clinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.clinic.model.DoctorAvailability;
import org.example.clinic.service.DoctorAvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;

    public Appointment save(Appointment appointment) {
        boolean available = doctorAvailabilityService.getDoctorAvailability(appointment.getDoctor())
                .stream()
                .anyMatch(a -> !appointment.getDate().isBefore(a.getAvailableFrom()) && !appointment.getDate().isAfter(a.getAvailableTo()));
        if (!available) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lekarz nie jest dostępny w wybranym terminie");
        }
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public void cancelById(Long id) {
        appointmentRepository.deleteById(id);
    }
}