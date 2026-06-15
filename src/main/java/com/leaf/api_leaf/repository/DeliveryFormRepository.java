package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.model.DeliveryForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface DeliveryFormRepository extends JpaRepository<DeliveryForm, Long> {

    long countByDate(LocalDate date);

    @Query("SELECT d FROM DeliveryForm d WHERE " +
           "(:date IS NULL OR d.date = :date) AND " +
           "(:driver IS NULL OR LOWER(d.driver) LIKE LOWER(CONCAT('%', :driver, '%'))) AND " +
           "(:customer IS NULL OR EXISTS (SELECT 1 FROM DeliveryFormRow r WHERE r.deliveryForm = d AND LOWER(r.customer) LIKE LOWER(CONCAT('%', :customer, '%'))))")
    Page<DeliveryForm> findByFilters(@Param("date") LocalDate date,
                                      @Param("driver") String driver,
                                      @Param("customer") String customer,
                                      Pageable pageable);
}
