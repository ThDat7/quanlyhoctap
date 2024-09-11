package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.NewsResponse;
import com.thanhdat.quanlyhoctap.dto.response.NewsViewResponse;

import java.util.Map;

public interface NewsService {
    DataWithCounterDto<NewsResponse> getAll(Map<String, String> params);

    NewsViewResponse get(Integer id);
}