package com.example.studenthub.repository;

import com.example.studenthub.dto.DepartmentSummary;
import com.example.studenthub.entity.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentCodeIgnoreCase(String code);
    Optional<Department> findByDepartmentNameIgnoreCase(String name);

    @Query("SELECT new com.example.studenthub.dto.DepartmentSummary(d.departmentCode, d.departmentName, COUNT(s)) "
         + "FROM Department d LEFT JOIN Student s ON s.department = d "
         + "GROUP BY d.departmentId, d.departmentCode, d.departmentName ORDER BY d.departmentName")
    List<DepartmentSummary> summary();
}