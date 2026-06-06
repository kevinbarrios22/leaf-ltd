package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.model.DeliverySheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeliverySheetRepository extends JpaRepository<DeliverySheet, Long> {
    List<DeliverySheet> findByDeliveryDate(LocalDate date);
    List<DeliverySheet> findByDriverName(String driverName);
}
