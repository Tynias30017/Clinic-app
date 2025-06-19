package org.example.clinic.controller;

import org.example.clinic.model.Appointment;
import org.example.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Umów wizytę (tylko dostępne terminy)")
    @PostMapping
    public Appointment save(@RequestBody Appointment appointment) {
        return appointmentService.save(appointment);
    }

    @Operation(summary = "Pobierz wszystkie wizyty")
    @GetMapping
    public List<Appointment> findAll() {
        return appointmentService.findAll();
    }
}