package com.example.studenthub.controller;

import com.example.studenthub.dto.*;
import com.example.studenthub.service.AcademicService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/academic-records")
public class AcademicRecordController {
    private final AcademicService academicService;

    public AcademicRecordController(AcademicService academicService) { this.academicService = academicService; }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public List<AcademicRecordResponse> byStudent(@RequestParam(name = "studentId") Long studentId) {
        return academicService.listByStudent(studentId);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public List<AcademicRecordResponse> mine(Authentication auth) { return academicService.listForUsername(auth.getName()); }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicRecordResponse create(@Valid @RequestBody AcademicRecordRequest request) { return academicService.create(request); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public AcademicRecordResponse update(@PathVariable("id") Long id, @Valid @RequestBody AcademicRecordRequest request) {
        return academicService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) { academicService.delete(id); }
}