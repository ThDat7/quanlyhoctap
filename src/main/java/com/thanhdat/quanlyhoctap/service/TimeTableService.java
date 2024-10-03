package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.ScheduleStudyClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.dto.request.ScheduleStudyRequest;
import com.thanhdat.quanlyhoctap.dto.response.ClassroomResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseClassScheduleResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseClassWithStatusResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudentClassWithStatusResponse;

import java.util.List;

public interface TimeTableService {
    List<CourseClassScheduleResponse> getByCurrentStudentAndSemester(Long semesterId);

    List<CourseClassScheduleResponse> getByCurrentTeacherAndSemester(Long semesterId);

    List<StudentClassWithStatusResponse> getStudentClassesWithStatus(Long semesterId);

    List<CourseClassWithStatusResponse> getCourseClassWithStatus(Long semesterId, Long studentClassId);

    List<CourseClassScheduleResponse> getBySemesterAndStudentClass(Long semesterId, Long studentClassId);

    void create(Long courseClassId, ScheduleStudyRequest request);

    List<ClassroomResponse> getAvailableClassrooms(Long courseClassId, ScheduleStudyClassroomAvailableRequest request);

    void delete(Long scheduleStudyId);
}
