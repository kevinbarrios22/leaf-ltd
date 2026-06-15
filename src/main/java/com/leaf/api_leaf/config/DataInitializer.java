package com.leaf.api_leaf.config;

import com.leaf.api_leaf.enums.RoleName;
import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.model.Role;
import com.leaf.api_leaf.repository.RoleRepository;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }

        if (!userRepository.existsByUsername("admin")) {
            Role bossRole = roleRepository.findByName(RoleName.BOSS).orElseThrow();

            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.getRoles().add(bossRole);

            userRepository.save(admin);
            log.info("Admin user created — user: admin / pass: admin123");
        }
    }
}