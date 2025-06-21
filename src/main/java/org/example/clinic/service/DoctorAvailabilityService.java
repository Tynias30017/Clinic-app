package org.example.clinic.service;

import lombok.RequiredArgsConstructor;
import org.example.clinic.model.DoctorAvailability;
import org.example.clinic.model.User;
import org.example.clinic.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityService {
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorAvailability addAvailability(DoctorAvailability availability) {
        return doctorAvailabilityRepository.save(availability);
    }

    public List<DoctorAvailability> getDoctorAvailability(User doctor) {
        return doctorAvailabilityRepository.findByDoctor(doctor);
    }

    public List<DoctorAvailability> getAllAvailability() {
        return doctorAvailabilityRepository.findAll();
    }

    public DoctorAvailability updateAvailability(Long id, DoctorAvailability updatedAvailability) {
        return doctorAvailabilityRepository.findById(id)
                .map(existing -> {
                    existing.setAvailableFrom(updatedAvailability.getAvailableFrom());
                    existing.setAvailableTo(updatedAvailability.getAvailableTo());
                    existing.setDoctor(updatedAvailability.getDoctor());
                    return doctorAvailabilityRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Doctor availability not found with id: " + id));
    }

    public void deleteAvailabilityById(Long id) {
        doctorAvailabilityRepository.deleteById(id);
    }
}
