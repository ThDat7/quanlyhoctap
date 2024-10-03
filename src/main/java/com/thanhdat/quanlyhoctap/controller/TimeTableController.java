package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.ScheduleStudyClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.dto.request.ScheduleStudyRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.TimeTableService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableController {
    TimeTableService timeTableService;

    @GetMapping("/semester/{semesterId}/current-student")
    public ApiResponse<List<CourseClassScheduleResponse>> getByCurrentStudentAndSemester(@PathVariable Long semesterId) {
        return ApiResponse.ok(timeTableService.getByCurrentStudentAndSemester(semesterId));
    }


    @GetMapping("/semester/{semesterId}/current-teacher")
    public ApiResponse<List<CourseClassScheduleResponse>> getByCurrentTeacherAndSemester(@PathVariable Long semesterId) {
        return ApiResponse.ok(timeTableService.getByCurrentTeacherAndSemester(semesterId));
    }

    @GetMapping("/semester/{semesterId}/student-classes-with-status")
    public ApiResponse<List<StudentClassWithStatusResponse>> getStudentClassesWithStatus(@PathVariable Long semesterId) {
        return ApiResponse.ok(timeTableService.getStudentClassesWithStatus(semesterId));
    }

    @GetMapping("/semester/{semesterId}/student-class/{studentClassId}/course-classes-with-status")
    public ApiResponse<List<CourseClassWithStatusResponse>> getStudentClassesWithStatus(@PathVariable Long semesterId,
                                                                                        @PathVariable Long studentClassId) {
        return ApiResponse.ok(timeTableService.getCourseClassWithStatus(semesterId, studentClassId));
    }

    @GetMapping("/semester/{semesterId}/student-class/{studentClassId}/schedules")
    public ApiResponse<List<CourseClassScheduleResponse>> getBySemesterAndStudentClass(@PathVariable Long semesterId,
                                                                                       @PathVariable Long studentClassId) {
        return ApiResponse.ok(timeTableService.getBySemesterAndStudentClass(semesterId, studentClassId));
    }

    @PostMapping("/course-class/{courseClassId}")
    public ApiResponse<Void> create(@PathVariable Long courseClassId, @RequestBody @Valid ScheduleStudyRequest request) {
        timeTableService.create(courseClassId, request);
        return ApiResponse.ok();
    }

    @PostMapping("/course-class/{courseClassId}/available-classrooms")
    public ApiResponse<List<ClassroomResponse>> getAvailableClassrooms(@PathVariable Long courseClassId,
                                                                       @RequestBody @Valid ScheduleStudyClassroomAvailableRequest request) {
        return ApiResponse.ok(timeTableService.getAvailableClassrooms(courseClassId, request));
    }

    @DeleteMapping("/schedule-study/{scheduleStudyId}")
    public ApiResponse<Void> delete(@PathVariable Long scheduleStudyId) {
        timeTableService.delete(scheduleStudyId);
        return ApiResponse.ok();
    }
}

