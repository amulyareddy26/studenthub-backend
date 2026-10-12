package com.example.studenthub.service;

import com.example.studenthub.dto.*;
import java.util.List;

public interface AcademicService {
    List<AcademicRecordResponse> listByStudent(Long studentId);
    List<AcademicRecordResponse> listForUsername(String username);
    AcademicRecordResponse create(AcademicRecordRequest request);
    AcademicRecordResponse update(Long id, AcademicRecordRequest request);
    void delete(Long id);
}