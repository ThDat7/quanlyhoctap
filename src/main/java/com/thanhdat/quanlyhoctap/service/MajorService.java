package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.MajorCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.MajorCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.MajorViewCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;

import java.util.List;
import java.util.Map;

public interface MajorService {
    DataWithCounterDto<MajorCrudResponse> getAll(Map<String, String> params);

    void create(MajorCrudRequest majorCrudRequest);

    void update(Long id, MajorCrudRequest majorCrudRequest);

    void delete(Long id);

    MajorViewCrudResponse getById(Long id);

    List<SelectOptionResponse> getSelectOptions();
}
