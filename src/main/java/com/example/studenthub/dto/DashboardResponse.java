package com.example.studenthub.dto;

import java.util.List;

public record DashboardResponse(long totalStudents, long totalDepartments, long totalUsers,
                                List<DepartmentSummary> departmentSummary, List<StudentResponse> recentStudents) {}