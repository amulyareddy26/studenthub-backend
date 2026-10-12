package com.example.studenthub.controller;

import com.example.studenthub.dto.DashboardResponse;
import com.example.studenthub.dto.DepartmentSummary;
import com.example.studenthub.service.ReportService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) { this.reportService = reportService; }

    @GetMapping("/api/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public DashboardResponse dashboard() { return reportService.dashboard(); }

    @GetMapping("/api/reports/department-summary")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public List<DepartmentSummary> departmentSummary() { return reportService.departmentSummary(); }
}