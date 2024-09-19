package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.CourseCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface CourseService {
    void create(CourseCrudRequest createRequest);
    void delete(Long id);
    DataWithCounterDto<CourseCrudResponse> getAll(Map<String, String> params);
    CourseViewCrudResponse getById(Long id);
    void update(Long id, CourseCrudRequest updateRequest);

    List<SelectOptionResponse> getTypes();

    List<SelectOptionResponse> getSelectOptions();
}
