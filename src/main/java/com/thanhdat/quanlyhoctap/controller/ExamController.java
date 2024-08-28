package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;
import com.thanhdat.quanlyhoctap.service.ExamService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exam-schedules")
@AllArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/semester/{semesterId}/current-student")
    public ResponseEntity<List<ExamScheduleResponse>> getByCurrentStudentAndSemester(@PathVariable Integer semesterId) {
        return ResponseEntity.ok(examService.getByCurrentStudentAndSemester(semesterId));
    }
}
