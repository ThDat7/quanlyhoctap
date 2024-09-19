package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.AvailableDateForMidtermExamResponse;
import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;
import com.thanhdat.quanlyhoctap.dto.response.MidtermExamResponse;
import com.thanhdat.quanlyhoctap.service.ExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-schedules")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamController {
    ExamService examService;

    @GetMapping("/semester/{semesterId}/current-student")
    public ResponseEntity<List<ExamScheduleResponse>> getByCurrentStudentAndSemester(@PathVariable Integer semesterId) {
        return ResponseEntity.ok(examService.getByCurrentStudentAndSemester(semesterId));
    }

    @GetMapping("/semester/{semesterId}/current-teacher")
    public ResponseEntity<List<MidtermExamResponse>> getByCurrentTeacherAndSemester(@PathVariable Integer semesterId) {
        return ResponseEntity.ok(examService.getByCurrentTeacherAndSemester(semesterId));
    }

    @GetMapping("/course-class/{courseClassId}/available-date-midterm-exam")
    public ResponseEntity<List<AvailableDateForMidtermExamResponse>> getAvailableDateMidtermExam(@PathVariable Integer courseClassId) {
        return ResponseEntity.ok(examService.getAvailableDateMidtermExam(courseClassId));
    }

    @PostMapping("/course-class/{courseClassId}/midterm-exam/current-teacher")
    public ResponseEntity updateMidtermExam(@PathVariable Integer courseClassId, @RequestBody UpdateMidtermExamRequest updateMidtermExamRequest) {
        examService.updateMidtermExamCurrentTeacher(courseClassId, updateMidtermExamRequest);
        return ResponseEntity.ok().build();
    }
}
