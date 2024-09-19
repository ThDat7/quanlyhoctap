package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.TeacherCourseClassTeachingResponse;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/course-classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClassController {
    CourseClassService courseClassService;

    @GetMapping("/semester/{semesterId}/current-teacher-teaching")
    public ResponseEntity<List<TeacherCourseClassTeachingResponse>> getCurrentTeacherTeachingBySemesterCourseClass(@PathVariable Long semesterId){
        return ResponseEntity.ok(courseClassService.getCurrentTeacherTeaching(semesterId));
    }
}
