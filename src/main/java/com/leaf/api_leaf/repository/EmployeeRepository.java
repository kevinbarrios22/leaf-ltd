package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByAppUserId(Long appUserId);
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Employee> findByActiveTrue(Pageable pageable);
    long countByActiveTrue();
}
