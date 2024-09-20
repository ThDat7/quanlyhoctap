package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudentCourseRegisterResponse;
import com.thanhdat.quanlyhoctap.service.CourseRegisterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-registers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseRegisterController {
    CourseRegisterService courseRegisterService;

    @GetMapping("/by-current-education-program")
    public ApiResponse<StudentCourseRegisterResponse> getStudentCourseRegisterInfo() {
        return ApiResponse.ok(courseRegisterService.getStudentCourseRegisterInfo());
    }

    @PostMapping("/register-course/{courseClassId}")
    public ApiResponse<StudentCourseRegisterResponse> registerCourse(@PathVariable Long courseClassId) {
        courseRegisterService.registerCourse(courseClassId);
        return getStudentCourseRegisterInfo();
    }

    @PostMapping("/unregister-course/{courseClassId}")
    public ApiResponse<StudentCourseRegisterResponse> unregisterCourse(@PathVariable Long courseClassId) {
        courseRegisterService.unregisterCourse(courseClassId);
        return getStudentCourseRegisterInfo();
    }
}
