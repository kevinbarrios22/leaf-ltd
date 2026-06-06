package com.leaf.api_leaf.config;

import com.leaf.api_leaf.enums.RoleName;
import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.model.Role;
import com.leaf.api_leaf.repository.RoleRepository;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Crear roles si no existen
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }

        // Crear usuario JEFE por defecto si no existe
        if (!userRepository.existsByUsername("admin")) {
            Role jefeRole = roleRepository.findByName(RoleName.BOSS).orElseThrow();

            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrador");
            admin.getRoles().add(jefeRole);

            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado — user: admin / pass: admin123");
        }
    }
}