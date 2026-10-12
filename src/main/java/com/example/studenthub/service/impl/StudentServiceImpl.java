package com.example.studenthub.service.impl;

import com.example.studenthub.dto.*;
import com.example.studenthub.entity.Department;
import com.example.studenthub.entity.Student;
import com.example.studenthub.exception.ConflictException;
import com.example.studenthub.exception.ResourceNotFoundException;
import com.example.studenthub.repository.AcademicRecordRepository;
import com.example.studenthub.repository.DepartmentRepository;
import com.example.studenthub.repository.StudentRepository;
import com.example.studenthub.service.StudentService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Set<String> SORT_FIELDS = Set.of("studentId", "rollNo", "name", "email", "year", "createdAt");

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final AcademicRecordRepository academicRecordRepository;

    public StudentServiceImpl(StudentRepository studentRepository, DepartmentRepository departmentRepository,
                              AcademicRecordRepository academicRecordRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.academicRecordRepository = academicRecordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StudentResponse> search(String search, Long departmentId, Integer year, String gender,
                                                int page, int size, String sortBy, String direction) {
        final String q = (search == null || search.isBlank()) ? null : search.trim().toLowerCase();
        final String g = (gender == null || gender.isBlank()) ? null : gender.trim();

        Specification<Student> spec = (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (q != null) {
                String like = "%" + q + "%";
                p.add(cb.or(cb.like(cb.lower(root.get("name")), like),
                            cb.like(cb.lower(root.get("rollNo")), like),
                            cb.like(cb.lower(root.get("email")), like)));
            }
            if (departmentId != null) p.add(cb.equal(root.get("department").get("departmentId"), departmentId));
            if (year != null) p.add(cb.equal(root.get("year"), year));
            if (g != null) p.add(cb.equal(root.get("gender"), g));
            return cb.and(p.toArray(new Predicate[0]));
        };

        String field = SORT_FIELDS.contains(sortBy) ? sortBy : "studentId";
        Sort sort = "asc".equalsIgnoreCase(direction) ? Sort.by(field).ascending() : Sort.by(field).descending();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 1000), sort);
        Page<Student> result = studentRepository.findAll(spec, pageable);
        return PageResponse.from(result.map(StudentResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return StudentResponse.from(find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getByRollNo(String rollNo) {
        return StudentResponse.from(studentRepository.findByRollNoIgnoreCase(rollNo)
                .orElseThrow(() -> new ResourceNotFoundException("No student profile is linked to this account")));
    }

    @Override
    @Transactional
    public StudentResponse create(StudentRequest r) {
        if (studentRepository.existsByRollNoIgnoreCase(r.rollNo().trim())) throw new ConflictException("Roll number already exists");
        if (studentRepository.existsByEmailIgnoreCase(r.email().trim())) throw new ConflictException("Email already exists");
        Student s = new Student();
        apply(s, r, findDepartment(r.departmentId()));
        return StudentResponse.from(studentRepository.save(s));
    }

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentRequest r) {
        Student s = find(id);
        studentRepository.findByRollNoIgnoreCase(r.rollNo().trim()).filter(o -> !o.getStudentId().equals(id))
                .ifPresent(o -> { throw new ConflictException("Roll number already exists"); });
        studentRepository.findByEmailIgnoreCase(r.email().trim()).filter(o -> !o.getStudentId().equals(id))
                .ifPresent(o -> { throw new ConflictException("Email already exists"); });
        apply(s, r, findDepartment(r.departmentId()));
        return StudentResponse.from(studentRepository.save(s));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student s = find(id);
        academicRecordRepository.deleteByStudentStudentId(id);
        studentRepository.delete(s);
    }

    private Student find(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
    }

    private void apply(Student s, StudentRequest r, Department d) {
        s.setRollNo(r.rollNo().trim());
        s.setName(r.name().trim());
        s.setEmail(r.email().trim());
        s.setPhone(r.phone().trim());
        s.setGender(r.gender());
        s.setYear(r.year());
        s.setDepartment(d);
    }
}