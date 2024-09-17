package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.List;
import java.util.Map;

public interface EducationProgramService  {
    DataWithCounterDto<EducationProgramSearchDto> search(Map<String, String> params);

    EducationProgramViewDto getView(int id);

    EducationProgramCloneBatchingResponse cloneBatching(int fromYear, int toYear);

    DataWithCounterDto<EducationProgramCrudResponse> getAll(Map<String, String> params);

    void create(EducationProgramCrudRequest createRequest);

    void delete(Integer id);

    EducationProgramViewCrudResponse getById(Integer id);

    void update(Integer id, EducationProgramCrudRequest updateRequest);
}