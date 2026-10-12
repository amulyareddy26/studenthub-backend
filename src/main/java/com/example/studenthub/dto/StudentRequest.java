package com.example.studenthub.dto;

import jakarta.validation.constraints.*;

public record StudentRequest(
        @NotBlank(message = "Roll number is required") @Size(max = 30, message = "Roll number max 30 chars") String rollNo,
        @NotBlank(message = "Name is required")
        @Pattern(regexp = "^[A-Za-z][A-Za-z .'-]{1,99}$", message = "Name contains invalid characters") String name,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") @Size(max = 120) String email,
        @NotBlank(message = "Phone is required") @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits") String phone,
        @NotBlank(message = "Gender is required") @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female or Other") String gender,
        @NotNull(message = "Year is required") @Min(value = 1, message = "Year must be 1-4") @Max(value = 4, message = "Year must be 1-4") Integer year,
        @NotNull(message = "Department is required") Long departmentId) {}