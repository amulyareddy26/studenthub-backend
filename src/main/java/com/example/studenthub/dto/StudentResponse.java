package com.example.studenthub.dto;

import com.example.studenthub.entity.Student;
import java.time.LocalDateTime;

public record StudentResponse(Long studentId, String rollNo, String name, String email, String phone, String gender,
                              Integer year, Long departmentId, String departmentCode, String departmentName,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static StudentResponse from(Student s) {
        return new StudentResponse(s.getStudentId(), s.getRollNo(), s.getName(), s.getEmail(), s.getPhone(), s.getGender(),
                s.getYear(), s.getDepartment().getDepartmentId(), s.getDepartment().getDepartmentCode(),
                s.getDepartment().getDepartmentName(), s.getCreatedAt(), s.getUpdatedAt());
    }
}