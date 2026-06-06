package com.leaf.api_leaf.dto.response;

import com.leaf.api_leaf.model.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponse {
    private Long id;
    private String fullName;
    private String email;
    private String position;
    private String phone;
    private boolean active;
    private String username;

    public static EmployeeResponse from(Employee emp) {
        EmployeeResponse dto = new EmployeeResponse();
        dto.setId(emp.getId());
        dto.setFullName(emp.getFullName());
        dto.setEmail(emp.getEmail());
        dto.setPosition(emp.getPosition());
        dto.setPhone(emp.getPhone());
        dto.setActive(emp.isActive());
        if (emp.getAppUser() != null) {
            dto.setUsername(emp.getAppUser().getUsername());
        }
        return dto;
    }
}