package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.StudentStatusResponse;

import java.util.List;

public interface StudentStatusService {
    List<StudentStatusResponse> getByCurrentStudent();
}
