package com.example.studenthub.service;

import com.example.studenthub.dto.*;
import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> list();
    DepartmentResponse get(Long id);
    DepartmentResponse create(DepartmentRequest request);
    DepartmentResponse update(Long id, DepartmentRequest request);
    void delete(Long id);
}