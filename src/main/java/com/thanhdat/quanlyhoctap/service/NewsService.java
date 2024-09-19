package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.NewsCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.Map;

public interface NewsService {
    DataWithCounterDto<NewsResponse> getAll(Map<String, String> params);

    NewsViewResponse get(Long id);


    void create(NewsCrudRequest createRequest);

    void delete(Long id);

    NewsViewCrudResponse getById(Long id);

    void update(Long id, NewsCrudRequest updateRequest);

    DataWithCounterDto<NewsCrudResponse> getAllCrud(Map<String, String> params);
}