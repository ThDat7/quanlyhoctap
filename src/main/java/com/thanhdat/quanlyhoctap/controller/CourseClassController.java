package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.dto.response.TeacherCourseClassTeachingResponse;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
