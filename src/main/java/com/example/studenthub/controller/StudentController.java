package com.example.studenthub.controller;

import com.example.studenthub.dto.*;
import com.example.studenthub.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) { this.studentService = studentService; }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public PageResponse<StudentResponse> list(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "departmentId", required = false) Long departmentId,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "gender", required = false) String gender,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "studentId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        return studentService.search(search, departmentId, year, gender, page, size, sortBy, direction);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public StudentResponse me(Authentication auth) { return studentService.getByRollNo(auth.getName()); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public StudentResponse get(@PathVariable("id") Long id) { return studentService.getById(id); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest request) { return studentService.create(request); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public StudentResponse update(@PathVariable("id") Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) { studentService.delete(id); }
}