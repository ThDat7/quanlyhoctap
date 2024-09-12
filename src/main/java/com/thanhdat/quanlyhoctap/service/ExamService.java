package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.AvailableDateForMidtermExamResponse;
import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;
import com.thanhdat.quanlyhoctap.dto.response.MidtermExamResponse;

import java.util.List;

public interface ExamService {

    List<ExamScheduleResponse> getByCurrentStudentAndSemester(Integer semesterId);

    List<MidtermExamResponse> getByCurrentTeacherAndSemester(Integer semesterId);

    List<AvailableDateForMidtermExamResponse> getAvailableDateMidtermExam(Integer courseClassId);

    void updateMidtermExamCurrentTeacher(Integer courseClassId, UpdateMidtermExamRequest updateMidtermExamRequest);
}
