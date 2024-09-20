package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.ExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-schedules")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamController {
    ExamService examService;

    @GetMapping("/semester/{semesterId}/current-student")
    public ApiResponse<List<ExamScheduleResponse>> getByCurrentStudentAndSemester(@PathVariable Long semesterId) {
        return ApiResponse.ok(examService.getByCurrentStudentAndSemester(semesterId));
    }

    @GetMapping("/semester/{semesterId}/current-teacher")
    public ApiResponse<List<MidtermExamResponse>> getByCurrentTeacherAndSemester(@PathVariable Long semesterId) {
        return ApiResponse.ok(examService.getByCurrentTeacherAndSemester(semesterId));
    }

    @GetMapping("/course-class/{courseClassId}/available-date-midterm-exam")
    public ApiResponse<List<AvailableDateForMidtermExamResponse>> getAvailableDateMidtermExam(
                                                                    @PathVariable Long courseClassId) {
        return ApiResponse.ok(examService.getAvailableDateMidtermExam(courseClassId));
    }

    @PostMapping("/course-class/{courseClassId}/midterm-exam/current-teacher")
    public ApiResponse<Void> updateMidtermExam(@PathVariable Long courseClassId,
                                               @RequestBody UpdateMidtermExamRequest updateMidtermExamRequest) {
        examService.updateMidtermExamCurrentTeacher(courseClassId, updateMidtermExamRequest);
        return ApiResponse.ok();
    }
}
