package com.leaf.api_leaf.repository;

import com.leaf.api_leaf.enums.RoleName;
import com.leaf.api_leaf.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}