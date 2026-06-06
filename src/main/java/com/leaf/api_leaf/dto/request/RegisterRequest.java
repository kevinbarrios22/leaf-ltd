package com.leaf.api_leaf.dto.request;

import com.leaf.api_leaf.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "The user is required")
    private String username;

    @NotBlank(message = "The password is required")
    private String password;

    @NotBlank(message = "The full name is required")
    private String fullName;

    @NotNull(message = "The rol is required")
    private RoleName role;
}