package com.leaf.api_leaf.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_forms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String person;

    @Column(nullable = false)
    private String driver;

    @Column(nullable = false)
    private String plate;

    private LocalTime time;

    @Column(nullable = false)
    private LocalDate deliveryDate;

    @OneToMany(mappedBy = "deliveryForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryFormRow> rows = new ArrayList<>();
}
