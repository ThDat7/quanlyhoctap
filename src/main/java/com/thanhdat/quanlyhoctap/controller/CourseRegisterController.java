package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.StudentCourseRegisterResponse;
import com.thanhdat.quanlyhoctap.service.CourseRegisterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-registers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseRegisterController {
    CourseRegisterService courseRegisterService;

    @GetMapping("/by-current-education-program")
    public ResponseEntity<StudentCourseRegisterResponse> getStudentCourseRegisterInfo() {
        return ResponseEntity.ok(courseRegisterService.getStudentCourseRegisterInfo());
    }

    @PostMapping("/register-course/{courseClassId}")
    public ResponseEntity<StudentCourseRegisterResponse> registerCourse(@PathVariable Integer courseClassId) {
        courseRegisterService.registerCourse(courseClassId);
        return getStudentCourseRegisterInfo();
    }

    @PostMapping("/unregister-course/{courseClassId}")
    public ResponseEntity<StudentCourseRegisterResponse> unregisterCourse(@PathVariable Integer courseClassId) {
        courseRegisterService.unregisterCourse(courseClassId);
        return getStudentCourseRegisterInfo();
    }
}
