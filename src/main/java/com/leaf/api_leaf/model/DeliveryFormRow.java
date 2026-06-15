package com.leaf.api_leaf.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_form_rows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFormRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customer;

    @Column(nullable = false)
    private int quantity;

    private boolean delivered;

    @ManyToOne
    @JoinColumn(name = "delivery_form_id", nullable = false)
    private DeliveryForm deliveryForm;
}
