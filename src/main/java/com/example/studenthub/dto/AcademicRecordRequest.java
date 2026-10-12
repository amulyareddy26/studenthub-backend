package com.example.studenthub.dto;

import jakarta.validation.constraints.*;

public record AcademicRecordRequest(
        @NotNull(message = "Student is required") Long studentId,
        @NotNull(message = "Semester is required") @Min(1) @Max(value = 8, message = "Semester must be 1-8") Integer semester,
        @NotNull(message = "CGPA is required") @DecimalMin("0.0") @DecimalMax(value = "10.0", message = "CGPA must be 0-10") Double cgpa,
        @NotNull(message = "Attendance is required") @DecimalMin("0.0") @DecimalMax(value = "100.0", message = "Attendance must be 0-100") Double attendance,
        @Size(max = 255) String remarks) {}