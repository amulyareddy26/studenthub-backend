package com.example.studenthub.repository;

import com.example.studenthub.entity.AcademicRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {
    List<AcademicRecord> findByStudentStudentIdOrderBySemesterAsc(Long studentId);
    void deleteByStudentStudentId(Long studentId);
}