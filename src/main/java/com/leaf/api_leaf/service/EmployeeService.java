package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.EmployeeDTO;
import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.model.Employee;
import com.leaf.api_leaf.repository.EmployeeRepository;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public Employee create(EmployeeDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un empleado con ese email");
        }

        Employee employee = new Employee();
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setPhone(dto.getPhone());

        if (dto.getAppUserId() != null) {
            
            AppUser user = userRepository.findById(dto.getAppUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            employee.setAppUser(user);
        }

        return employeeRepository.save(employee);
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    public Employee update(Long id, EmployeeDTO dto) {
        Employee employee = getById(id);
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setPhone(dto.getPhone());

        if (dto.getAppUserId() != null) {
            AppUser user = userRepository.findById(dto.getAppUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            employee.setAppUser(user);
        }

        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        Employee employee = getById(id);
        employee.setActive(false); // desactivar en lugar de borrar
        employeeRepository.save(employee);
    }
}