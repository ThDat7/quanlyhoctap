package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.TeacherCourseClassTeachingResponse;

import java.util.List;

public interface CourseClassService {
    List<TeacherCourseClassTeachingResponse> getCurrentTeacherTeaching(Integer semesterId);
}
