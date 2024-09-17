package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.NewsCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.Map;

public interface NewsService {
    DataWithCounterDto<NewsResponse> getAll(Map<String, String> params);

    NewsViewResponse get(Integer id);


    void create(NewsCrudRequest createRequest);

    void delete(Integer id);

    NewsViewCrudResponse getById(Integer id);

    void update(Integer id, NewsCrudRequest updateRequest);

    DataWithCounterDto<NewsCrudResponse> getAllCrud(Map<String, String> params);
}