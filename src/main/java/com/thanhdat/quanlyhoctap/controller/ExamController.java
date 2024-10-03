package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.GetRoomAvailableFinalExamRequest;
import com.thanhdat.quanlyhoctap.dto.request.UpdateFinalExamRequest;
import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.ExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/semester/{semesterId}/course-with-final-exam-schedule-status")
    public ApiResponse<List<CourseWithFinalExamScheduleStatusResponse>> getCourseWithFinalExamScheduleStatus(
                                                                @PathVariable Long semesterId,
                                                                @RequestParam Map<String, String> params) {
        return ApiResponse.ok(examService.getCourseWithFinalExamScheduleStatus(semesterId));
    }

    @GetMapping("/semester/{semesterId}/course/{courseId}/final-exam")
    public ApiResponse<DataWithCounterDto<FinalExamResponse>> getFinalExamByCourseAndSemester(@PathVariable Long semesterId,
                                                                                              @PathVariable Long courseId,
                                                                                              @RequestParam Map<String, String> params) {
        return ApiResponse.ok(examService.getFinalExamByCourseAndSemester(semesterId, courseId, params));
    }

    @GetMapping("/course-class/{courseClassId}/available-time-final-exam")
    public ApiResponse<AvailableTimeForFinalExamResponse> getAvailableTimeFinalExam(@PathVariable Long courseClassId,
                                                                                    @RequestParam(required = false)
                                                                                    Integer page) {
        return ApiResponse.ok(examService.getAvailableTimeFinalExam(courseClassId, page));
    }

    @PostMapping("/course-class/{courseClassId}/available-room-final-exam")
    public ApiResponse<List<ClassroomResponse>> getAvailableRoomForFinalExam(@PathVariable Long courseClassId,
                                                                           @RequestBody GetRoomAvailableFinalExamRequest
                                                                           request) {
        return ApiResponse.ok(examService.getAvailableRoomForFinalExam(courseClassId, request));
    }

    @PostMapping("/course-class/{courseClassId}/final-exam")
    public ApiResponse<Void> updateFinalExam(@PathVariable Long courseClassId,
                                            @RequestBody UpdateFinalExamRequest request) {
        examService.updateFinalExam(courseClassId, request);
        return ApiResponse.ok();
    }
}
