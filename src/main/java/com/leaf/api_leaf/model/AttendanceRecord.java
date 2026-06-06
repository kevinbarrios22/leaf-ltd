package com.leaf.api_leaf.model;

import com.leaf.api_leaf.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_records")
@Data
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime checkIn;
    private LocalTime checkOut;

    private LocalTime scheduledStart;
    private LocalTime scheduledEnd;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private String notes;
}