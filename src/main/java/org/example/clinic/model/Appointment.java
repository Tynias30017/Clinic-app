package org.example.clinic.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"patient", "doctor"})
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum Status {
        PENDING,
        CONFIRMED,
        COMPLETED,
        CANCELLED
    }
}