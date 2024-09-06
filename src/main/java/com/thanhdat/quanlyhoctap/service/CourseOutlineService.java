package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.CourseOutlineSearchDto;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;

import java.util.Map;

public interface CourseOutlineService {
    DataWithCounterDto<CourseOutlineSearchDto> search(Map<String, String> params);
}
