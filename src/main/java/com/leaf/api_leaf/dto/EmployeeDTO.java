package com.leaf.api_leaf.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {

    @NotBlank(message = "The name is required")
    private String fullName;

    @NotBlank(message = "The email is required")
    @Email(message = "Invalid email")
    private String email;

    private String position;
    private String phone;
    private Long appUserId; // Optional, to link with system user
}