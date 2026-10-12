package com.example.studenthub.service.impl;

import com.example.studenthub.dto.AcademicRecordRequest;
import com.example.studenthub.dto.AcademicRecordResponse;
import com.example.studenthub.entity.AcademicRecord;
import com.example.studenthub.entity.Student;
import com.example.studenthub.exception.ResourceNotFoundException;
import com.example.studenthub.repository.AcademicRecordRepository;
import com.example.studenthub.repository.StudentRepository;
import com.example.studenthub.service.AcademicService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcademicServiceImpl implements AcademicService {
    private final AcademicRecordRepository recordRepository;
    private final StudentRepository studentRepository;

    public AcademicServiceImpl(AcademicRecordRepository recordRepository, StudentRepository studentRepository) {
        this.recordRepository = recordRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicRecordResponse> listByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) throw new ResourceNotFoundException("Student not found with id " + studentId);
        return recordRepository.findByStudentStudentIdOrderBySemesterAsc(studentId).stream().map(AcademicRecordResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicRecordResponse> listForUsername(String username) {
        Student s = studentRepository.findByRollNoIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("No student profile is linked to this account"));
        return recordRepository.findByStudentStudentIdOrderBySemesterAsc(s.getStudentId()).stream().map(AcademicRecordResponse::from).toList();
    }

    @Override
    @Transactional
    public AcademicRecordResponse create(AcademicRecordRequest r) {
        Student s = studentRepository.findById(r.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + r.studentId()));
        AcademicRecord rec = new AcademicRecord();
        rec.setStudent(s);
        apply(rec, r);
        return AcademicRecordResponse.from(recordRepository.save(rec));
    }

    @Override
    @Transactional
    public AcademicRecordResponse update(Long id, AcademicRecordRequest r) {
        AcademicRecord rec = recordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Record not found with id " + id));
        apply(rec, r);
        return AcademicRecordResponse.from(recordRepository.save(rec));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        AcademicRecord rec = recordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Record not found with id " + id));
        recordRepository.delete(rec);
    }

    private void apply(AcademicRecord rec, AcademicRecordRequest r) {
        rec.setSemester(r.semester());
        rec.setCgpa(r.cgpa());
        rec.setAttendance(r.attendance());
        rec.setRemarks(r.remarks() == null ? null : r.remarks().trim());
    }
}