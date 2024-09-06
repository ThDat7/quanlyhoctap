package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.StudentCourseRegisterResponse;

public interface CourseRegisterService {
    StudentCourseRegisterResponse getStudentCourseRegisterInfo();

    void registerCourse(Integer courseClassId);

    void unregisterCourse(Integer courseClassId);
}
