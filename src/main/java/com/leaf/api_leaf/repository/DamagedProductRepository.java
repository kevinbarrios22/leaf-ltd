package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.model.DamagedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DamagedProductRepository extends JpaRepository<DamagedProduct, Long> {
    List<DamagedProduct> findByReportDate(LocalDate date);
    List<DamagedProduct> findByItemNumber(String itemNumber);
    List<DamagedProduct> findByReportDateBetween(LocalDate from, LocalDate to);

}
