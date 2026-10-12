package com.example.studenthub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(
        @NotBlank(message = "Department code is required") @Size(max = 20)
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Code must be letters/numbers only") String departmentCode,
        @NotBlank(message = "Department name is required") @Size(max = 100) String departmentName) {}