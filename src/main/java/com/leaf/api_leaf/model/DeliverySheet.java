package com.leaf.api_leaf.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_sheets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate deliveryDate;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String driverName;

    @Column(nullable = false)
    private String vehiclePlate;

    @OneToMany(mappedBy = "deliverySheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryInvoice> invoices = new ArrayList<>();
}