package com.leaf.api_leaf.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_records", uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime entryTime;
    private LocalTime exitTime;

    @Column(nullable = false)
    private String status;
}
