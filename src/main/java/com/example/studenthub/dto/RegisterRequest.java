package com.example.studenthub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Roll number is required") @Size(min = 3, max = 50) String username,
        @NotBlank(message = "Password is required") @Size(min = 6, max = 100, message = "Password must be 6-100 characters") String password) {}