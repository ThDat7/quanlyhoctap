package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.FacultyCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface FacultyService {
    List<SelectOptionResponse> getAllForSelect();

    void create(FacultyCrudRequest createRequest);

    void delete(Long id);

    DataWithCounterDto<FacultyCrudResponse> getAll(Map<String, String> params);

    FacultyViewCrudResponse getById(Long id);

    void update(Long id, FacultyCrudRequest updateRequest);
}
