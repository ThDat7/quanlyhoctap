package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.AvailableDateForMidtermExamResponse;
import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;
import com.thanhdat.quanlyhoctap.dto.response.MidtermExamResponse;

import java.util.List;

public interface ExamService {

    List<? extends ExamScheduleResponse> getByCurrentStudentAndSemester(Long semesterId);

    List<MidtermExamResponse> getByCurrentTeacherAndSemester(Long semesterId);

    List<AvailableDateForMidtermExamResponse> getAvailableDateMidtermExam(Long courseClassId);

    void updateMidtermExamCurrentTeacher(Long courseClassId, UpdateMidtermExamRequest updateMidtermExamRequest);
}
