package com.example.studenthub.service.impl;

import com.example.studenthub.dto.DepartmentRequest;
import com.example.studenthub.dto.DepartmentResponse;
import com.example.studenthub.entity.Department;
import com.example.studenthub.exception.ConflictException;
import com.example.studenthub.exception.ResourceNotFoundException;
import com.example.studenthub.repository.DepartmentRepository;
import com.example.studenthub.repository.StudentRepository;
import com.example.studenthub.service.DepartmentService;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, StudentRepository studentRepository) {
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> list() {
        return departmentRepository.findAll(Sort.by("departmentName")).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse get(Long id) { return toResponse(find(id)); }

    @Override
    @Transactional
    public DepartmentResponse create(DepartmentRequest r) {
        String code = r.departmentCode().trim().toUpperCase();
        String name = r.departmentName().trim();
        if (departmentRepository.findByDepartmentCodeIgnoreCase(code).isPresent()) throw new ConflictException("Department code already exists");
        if (departmentRepository.findByDepartmentNameIgnoreCase(name).isPresent()) throw new ConflictException("Department name already exists");
        return toResponse(departmentRepository.save(new Department(code, name)));
    }

    @Override
    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest r) {
        Department d = find(id);
        String code = r.departmentCode().trim().toUpperCase();
        String name = r.departmentName().trim();
        departmentRepository.findByDepartmentCodeIgnoreCase(code).filter(o -> !o.getDepartmentId().equals(id))
                .ifPresent(o -> { throw new ConflictException("Department code already exists"); });
        departmentRepository.findByDepartmentNameIgnoreCase(name).filter(o -> !o.getDepartmentId().equals(id))
                .ifPresent(o -> { throw new ConflictException("Department name already exists"); });
        d.setDepartmentCode(code);
        d.setDepartmentName(name);
        return toResponse(departmentRepository.save(d));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Department d = find(id);
        if (studentRepository.countByDepartmentDepartmentId(id) > 0) {
            throw new ConflictException("Cannot delete a department that still has students");
        }
        departmentRepository.delete(d);
    }

    private Department find(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
    }

    private DepartmentResponse toResponse(Department d) {
        return new DepartmentResponse(d.getDepartmentId(), d.getDepartmentCode(), d.getDepartmentName(),
                studentRepository.countByDepartmentDepartmentId(d.getDepartmentId()));
    }
}