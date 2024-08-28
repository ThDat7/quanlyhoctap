package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.CourseClassScheduleResponse;

import java.util.List;

public interface TimeTableService {
    List<CourseClassScheduleResponse> getByCurrentStudentAndSemester(Integer semesterId);
}
