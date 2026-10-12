package com.example.studenthub.dto;

import com.example.studenthub.entity.AcademicRecord;

public record AcademicRecordResponse(Long recordId, Long studentId, Integer semester, Double cgpa, Double attendance, String remarks) {
    public static AcademicRecordResponse from(AcademicRecord r) {
        return new AcademicRecordResponse(r.getRecordId(), r.getStudent().getStudentId(), r.getSemester(), r.getCgpa(),
                r.getAttendance(), r.getRemarks());
    }
}
