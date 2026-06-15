package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.model.DeliverySheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DeliverySheetRepository extends JpaRepository<DeliverySheet, Long> {
    Page<DeliverySheet> findByDeliveryDate(LocalDate date, Pageable pageable);
    Page<DeliverySheet> findByDriverName(String driverName, Pageable pageable);
}