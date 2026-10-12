package com.example.studenthub.service;

import com.example.studenthub.dto.*;

public interface StudentService {
    PageResponse<StudentResponse> search(String search, Long departmentId, Integer year, String gender,
                                         int page, int size, String sortBy, String direction);
    StudentResponse getById(Long id);
    StudentResponse getByRollNo(String rollNo);
    StudentResponse create(StudentRequest request);
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);
}