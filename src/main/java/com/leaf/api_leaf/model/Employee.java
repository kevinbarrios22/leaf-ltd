package com.leaf.api_leaf.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String position;
    private String phone;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    private boolean active = true;
}