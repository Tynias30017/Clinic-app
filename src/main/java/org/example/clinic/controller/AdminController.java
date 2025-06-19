package org.example.clinic.controller;

import lombok.RequiredArgsConstructor;
import org.example.clinic.model.Appointment;
import org.example.clinic.model.User;
import org.example.clinic.service.AppointmentService;
import org.example.clinic.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AppointmentService appointmentService;
    private final UserService userService;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        List<User> users = userService.getAllUsers();
        stats.put("totalUsers", users.size());
        stats.put("totalDoctors", users.stream().filter(u -> u.getRole() == User.Role.DOCTOR).count());
        stats.put("totalPatients", users.stream().filter(u -> u.getRole() == User.Role.PATIENT).count());
        stats.put("totalAppointments", appointmentService.findAll().size());
        return stats;
    }

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return appointmentService.findAll();
    }
}

