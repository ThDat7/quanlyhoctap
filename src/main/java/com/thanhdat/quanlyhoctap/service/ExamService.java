package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;

import java.util.List;

public interface ExamService {

    List<ExamScheduleResponse> getByCurrentStudentAndSemester(Integer semesterId);
}
