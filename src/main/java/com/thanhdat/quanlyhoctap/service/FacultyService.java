package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.FacultyCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface FacultyService {
    List<SelectOptionResponse> getAllForSelect();

    void create(FacultyCrudRequest createRequest);

    void delete(Integer id);

    DataWithCounterDto<FacultyCrudResponse> getAll(Map params);

    FacultyViewCrudResponse getById(Integer id);

    void update(Integer id, FacultyCrudRequest updateRequest);
}
