package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;

import java.util.Map;

public interface EducationProgramService  {
    DataWithCounterDto<EducationProgramSearchDto> search(Map<String, String> params);

    EducationProgramViewDto getView(Long id);

    EducationProgramCloneBatchingResponse cloneBatching(int fromYear, int toYear);

    DataWithCounterDto<EducationProgramCrudResponse> getAll(Map<String, String> params);

    void create(EducationProgramCrudRequest createRequest);

    void delete(Long id);

    EducationProgramViewCrudResponse getById(Long id);

    void update(Long id, EducationProgramCrudRequest updateRequest);
}