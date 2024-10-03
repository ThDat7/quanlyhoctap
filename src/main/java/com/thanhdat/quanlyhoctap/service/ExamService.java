package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.GetRoomAvailableFinalExamRequest;
import com.thanhdat.quanlyhoctap.dto.request.UpdateFinalExamRequest;
import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface ExamService {

    List<ExamScheduleResponse> getByCurrentStudentAndSemester(Long semesterId);

    List<MidtermExamResponse> getByCurrentTeacherAndSemester(Long semesterId);

    List<AvailableDateForMidtermExamResponse> getAvailableDateMidtermExam(Long courseClassId);

    void updateMidtermExamCurrentTeacher(Long courseClassId, UpdateMidtermExamRequest updateMidtermExamRequest);

    List<CourseWithFinalExamScheduleStatusResponse> getCourseWithFinalExamScheduleStatus(Long semesterId);

    DataWithCounterDto<FinalExamResponse> getFinalExamByCourseAndSemester(Long semesterId, Long courseId,
                                                                          Map<String, String> params);

    AvailableTimeForFinalExamResponse getAvailableTimeFinalExam(Long courseClassId, Integer page);

    List<ClassroomResponse> getAvailableRoomForFinalExam(Long courseClassId, GetRoomAvailableFinalExamRequest page);

    void updateFinalExam(Long courseClassId, UpdateFinalExamRequest request);
}
