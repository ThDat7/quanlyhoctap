package com.thanhdat.quanlyhoctap.controller;


import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course-classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClassController {
    CourseClassService courseClassService;

    @GetMapping("/semester/{semesterId}/current-teacher-teaching")
    public ApiResponse<List<TeacherCourseClassTeachingResponse>> getCurrentTeacherTeachingBySemesterCourseClass(@PathVariable Long semesterId){
        return ApiResponse.ok(courseClassService.getCurrentTeacherTeaching(semesterId));
    }

    @GetMapping("/semester/{semesterId}/course/{courseId}")
    public ApiResponse<DataWithCounterDto<CourseClassResponse>> getCourseClassBySemesterAndCourse(@PathVariable Long semesterId, @PathVariable Long courseId,
                                                                                                  @RequestParam Map<String, String> params) {
        return ApiResponse.ok(courseClassService.getCourseClassBySemesterAndCourse(semesterId, courseId, params));
    }
}
