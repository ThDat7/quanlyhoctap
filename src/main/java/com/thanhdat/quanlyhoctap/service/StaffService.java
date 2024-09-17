package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;

import java.util.List;

public interface StaffService {
    List<SelectOptionResponse> getSelectOptions();

}
