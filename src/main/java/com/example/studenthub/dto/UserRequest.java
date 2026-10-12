package com.example.studenthub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Username is required") @Size(min = 3, max = 50) String username,
        @Size(min = 6, max = 100, message = "Password must be 6-100 characters") String password,
        @NotBlank(message = "Role is required") @Pattern(regexp = "^(ADMIN|FACULTY|STUDENT)$", message = "Role must be ADMIN, FACULTY or STUDENT") String role,
        @NotBlank(message = "Status is required") @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Status must be ACTIVE or INACTIVE") String status) {}