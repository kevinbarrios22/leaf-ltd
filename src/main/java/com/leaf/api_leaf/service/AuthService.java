package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.response.AuthResponse;
import com.leaf.api_leaf.dto.request.LoginRequest;
import com.leaf.api_leaf.dto.request.RegisterRequest;
import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.model.Role;
import com.leaf.api_leaf.repository.RoleRepository;
import com.leaf.api_leaf.repository.UserRepository;
import com.leaf.api_leaf.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        AppUser user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().name())
                .orElse("EMPLEADO");

        return new AuthResponse(token, user.getUsername(), role);
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.getRoles().add(role);

        userRepository.save(user);
        return "Usuario registrado exitosamente";
    }
}