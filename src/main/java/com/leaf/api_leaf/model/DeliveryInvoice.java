package com.leaf.api_leaf.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "delivery_sheet_id", nullable = false)
    private DeliverySheet deliverySheet;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String invoiceNumber;

    private Integer invoiceCount;
}