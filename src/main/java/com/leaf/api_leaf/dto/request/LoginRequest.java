package com.leaf.api_leaf.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "The user is required")
    private String username;

    @NotBlank(message = "The password is required")
    private String password;

}
