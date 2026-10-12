package com.example.studenthub.service;

import com.example.studenthub.dto.*;
import java.util.List;

public interface ReportService {
    DashboardResponse dashboard();
    List<DepartmentSummary> departmentSummary();
}