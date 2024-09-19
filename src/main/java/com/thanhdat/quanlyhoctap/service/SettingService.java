package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.SettingCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.SettingCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SettingViewCrudResponse;

import java.util.Map;

public interface SettingService {
    Long getSemesterIdForRegister();

    SettingViewCrudResponse getById(String key);

    void update(String key, SettingCrudRequest updateRequest);

    DataWithCounterDto<SettingCrudResponse> getAll(Map<String, String> params);
}
