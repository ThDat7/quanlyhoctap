package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.StudentCourseRegisterResponse;
import com.thanhdat.quanlyhoctap.service.CourseRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-registers")
@AllArgsConstructor
public class CourseRegisterController {
    private final CourseRegisterService courseRegisterService;

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
