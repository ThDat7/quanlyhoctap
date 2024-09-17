package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.CourseCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface CourseService {
    void create(CourseCrudRequest createRequest);
    void delete(Integer id);
    DataWithCounterDto<CourseCrudResponse> getAll(Map<String, String> params);
    CourseViewCrudResponse getById(Integer id);
    void update(Integer id, CourseCrudRequest updateRequest);

    List<SelectOptionResponse> getTypes();

    List<SelectOptionResponse> getSelectOptions();
}
