package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.StudyResultSemesterResponse;

import java.util.List;

public interface StudyResultService {
    List<StudyResultSemesterResponse> getByCurrentStudent();
}
