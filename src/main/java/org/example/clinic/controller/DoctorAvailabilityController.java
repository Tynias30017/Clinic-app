package org.example.clinic.controller;

import lombok.RequiredArgsConstructor;
import org.example.clinic.model.DoctorAvailability;
import org.example.clinic.model.User;
import org.example.clinic.service.DoctorAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-availability")
@RequiredArgsConstructor
public class DoctorAvailabilityController {
    private final DoctorAvailabilityService doctorAvailabilityService;

    @PostMapping
    public DoctorAvailability addAvailability(@RequestBody DoctorAvailability availability) {
        return doctorAvailabilityService.addAvailability(availability);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<DoctorAvailability> getDoctorAvailability(@PathVariable Long doctorId) {
        User doctor = new User();
        doctor.setId(doctorId);
        return doctorAvailabilityService.getDoctorAvailability(doctor);
    }

    @GetMapping
    public List<DoctorAvailability> getAllAvailability() {
        return doctorAvailabilityService.getAllAvailability();
    }
}

