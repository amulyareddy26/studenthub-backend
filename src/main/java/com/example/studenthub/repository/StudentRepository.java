package com.example.studenthub.repository;

import com.example.studenthub.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    boolean existsByRollNoIgnoreCase(String rollNo);
    boolean existsByEmailIgnoreCase(String email);
    Optional<Student> findByRollNoIgnoreCase(String rollNo);
    Optional<Student> findByEmailIgnoreCase(String email);
    long countByDepartmentDepartmentId(Long departmentId);
    List<Student> findTop5ByOrderByCreatedAtDesc();
}