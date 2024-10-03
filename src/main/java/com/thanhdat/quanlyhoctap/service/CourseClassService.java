package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface CourseClassService {
    List<TeacherCourseClassTeachingResponse> getCurrentTeacherTeaching(Long semesterId);

    DataWithCounterDto<CourseClassResponse> getCourseClassBySemesterAndCourse(Long semesterId, Long courseId, Map<String, String> params);
}
