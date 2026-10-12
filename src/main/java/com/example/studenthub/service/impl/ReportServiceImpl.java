package com.example.studenthub.service.impl;

import com.example.studenthub.dto.DashboardResponse;
import com.example.studenthub.dto.DepartmentSummary;
import com.example.studenthub.dto.StudentResponse;
import com.example.studenthub.repository.DepartmentRepository;
import com.example.studenthub.repository.StudentRepository;
import com.example.studenthub.repository.UserRepository;
import com.example.studenthub.service.ReportService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl implements ReportService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public ReportServiceImpl(StudentRepository studentRepository, DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse dashboard() {
        return new DashboardResponse(studentRepository.count(), departmentRepository.count(), userRepository.count(),
                departmentRepository.summary(),
                studentRepository.findTop5ByOrderByCreatedAtDesc().stream().map(StudentResponse::from).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentSummary> departmentSummary() { return departmentRepository.summary(); }
}