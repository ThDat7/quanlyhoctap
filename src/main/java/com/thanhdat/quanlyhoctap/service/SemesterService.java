package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.SemesterResponse;

import java.util.List;

public interface SemesterService {
    List<SemesterResponse> getByCurrentStudent();

    List<SemesterResponse> getByCurrentTeacher();
}
