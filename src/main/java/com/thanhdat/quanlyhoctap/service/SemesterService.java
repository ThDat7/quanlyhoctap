package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.dto.response.SemesterDetailResponse;

import java.util.List;

public interface SemesterService {
    List<SemesterDetailResponse> getByCurrentStudent();

    List<SemesterDetailResponse> getByCurrentTeacher();

    List<SemesterDetailResponse> getNoneLocked();

    List<SemesterDetailResponse> getAll();
}
