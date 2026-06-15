package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.model.DamagedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DamagedProductRepository extends JpaRepository<DamagedProduct, Long> {
    Page<DamagedProduct> findByReportDate(LocalDate date, Pageable pageable);
    Page<DamagedProduct> findByItemNumber(String itemNumber, Pageable pageable);
    Page<DamagedProduct> findByReportDateBetween(LocalDate from, LocalDate to, Pageable pageable);
    long countByReportDate(LocalDate date);
}
