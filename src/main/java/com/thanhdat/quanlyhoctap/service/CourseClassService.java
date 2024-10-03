package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.CourseClassCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface CourseClassService {
    List<TeacherCourseClassTeachingResponse> getCurrentTeacherTeaching(Long semesterId);

    DataWithCounterDto<CourseClassResponse> getCourseClassBySemesterAndCourse(Long semesterId, Long courseId, Map<String, String> params);

    DataWithCounterDto<CourseWithCourseClassCountResponse> getCourseWithCourseClassCountBySemester(Long semesterId,
                                                                                                   Map<String, String> params);
    CourseClassViewCrudResponse getById(Long id);

    List<SelectOptionResponse> getSelectOptionsAvailableTeacher(Long courseId, Long semesterId);

    List<SelectOptionResponse> getSelectOptionsAvailableStudentClass(Long courseId, Long semesterId);

    void create(CourseClassCrudRequest createRequest);

    void delete(Long id);

    void update(Long id, CourseClassCrudRequest updateRequest);
}
