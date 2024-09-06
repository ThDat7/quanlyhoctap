package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramSearchDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramViewDto;

import java.util.Map;

public interface EducationProgramService {
    DataWithCounterDto<EducationProgramSearchDto> search(Map<String, String> params);

    EducationProgramViewDto getView(int id);
}