package com.leaf.api_leaf.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Table(name = "damaged_product")
@Data
public class DamagedProduct {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column (nullable = false)
    private LocalDate reportDate;

   @Column(nullable = false)
   private String itemNumber;

   @Column(nullable = false)
    private Integer quantity;

    private String description;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private AppUser reportedBy;
}
