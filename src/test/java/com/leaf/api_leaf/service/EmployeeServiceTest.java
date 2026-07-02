package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.EmployeeDTO;
import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.model.Employee;
import com.leaf.api_leaf.repository.EmployeeRepository;
import com.leaf.api_leaf.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService - Unit Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDTO dto;
    private Employee employee;

    // ─── Setup ────────────────────────────────────────────────────────────────

    @BeforeEach
    void setUp() {
        dto = new EmployeeDTO();
        dto.setFullName("Kevin Barrios");
        dto.setEmail("kevin@leaf.com");
        dto.setPosition("Developer");
        dto.setPhone("555-1234");
        dto.setAppUserId(null);

        employee = new Employee();
        employee.setId(1L);
        employee.setFullName("Kevin Barrios");
        employee.setEmail("kevin@leaf.com");
        employee.setPosition("Developer");
        employee.setPhone("555-1234");
        employee.setActive(true);
    }

    // ─── create() ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create: should save and return employee when email is not duplicated")
    void create_success() {
        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("Kevin Barrios");
        assertThat(result.getEmail()).isEqualTo("kevin@leaf.com");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("create: should throw RuntimeException when email already exists")
    void create_duplicateEmail_throwsException() {
        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> employeeService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("There is already an employee with this email");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: should link AppUser when appUserId is provided")
    void create_withAppUser_linksUser() {
        AppUser appUser = new AppUser();
        appUser.setId(10L);

        dto.setAppUserId(10L);

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.findById(10L)).thenReturn(Optional.of(appUser));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> {
            Employee e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        Employee result = employeeService.create(dto);

        assertThat(result.getAppUser()).isEqualTo(appUser);
        verify(userRepository).findById(10L);
    }

    @Test
    @DisplayName("create: should throw RuntimeException when appUserId does not exist")
    void create_withInvalidAppUserId_throwsException() {
        dto.setAppUserId(99L);

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");

        verify(employeeRepository, never()).save(any());
    }

    // ─── getAll() ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll: should return page of active employees")
    void getAll_returnsActivePage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(employee));

        when(employeeRepository.findByActiveTrue(pageable)).thenReturn(page);

        Page<Employee> result = employeeService.getAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("kevin@leaf.com");
    }

    @Test
    @DisplayName("getAll: should return empty page when no active employees exist")
    void getAll_emptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(employeeRepository.findByActiveTrue(pageable)).thenReturn(Page.empty());

        Page<Employee> result = employeeService.getAll(pageable);

        assertThat(result.isEmpty()).isTrue();
    }

    // ─── getById() ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById: should return employee when found")
    void getById_found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFullName()).isEqualTo("Kevin Barrios");
    }

    @Test
    @DisplayName("getById: should throw RuntimeException when not found")
    void getById_notFound_throwsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee not found");
    }

    // ─── update() ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update: should update and return employee when found")
    void update_success() {
        dto.setFullName("Kevin Updated");
        dto.setPhone("555-9999");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.update(1L, dto);

        assertThat(result.getFullName()).isEqualTo("Kevin Updated");
        assertThat(result.getPhone()).isEqualTo("555-9999");
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("update: should throw RuntimeException when employee not found")
    void update_notFound_throwsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(99L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee not found");

        verify(employeeRepository, never()).save(any());
    }

    // ─── delete() ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete: should set active=false (soft delete)")
    void delete_setsActiveFalse() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        employeeService.delete(1L);

        assertThat(employee.isActive()).isFalse();
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("delete: should throw RuntimeException when employee not found")
    void delete_notFound_throwsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.delete(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee not found");

        verify(employeeRepository, never()).save(any());
    }
}