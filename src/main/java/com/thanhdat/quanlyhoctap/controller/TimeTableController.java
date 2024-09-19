package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.CourseClassScheduleResponse;
import com.thanhdat.quanlyhoctap.service.TimeTableService;
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
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableController {
    TimeTableService timeTableService;

    @GetMapping("/semester/{semesterId}/current-student")
    public ResponseEntity<List<CourseClassScheduleResponse>> getByCurrentStudentAndSemester(@PathVariable Integer semesterId) {
        return ResponseEntity.ok(timeTableService.getByCurrentStudentAndSemester(semesterId));
    }


    @GetMapping("/semester/{semesterId}/current-teacher")
    public ResponseEntity<List<CourseClassScheduleResponse>> getByCurrentTeacherAndSemester(@PathVariable Integer semesterId) {
        return ResponseEntity.ok(timeTableService.getByCurrentTeacherAndSemester(semesterId));
    }
}
