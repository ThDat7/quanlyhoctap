package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.CourseOutlineEditTeacherRequest;
import com.thanhdat.quanlyhoctap.dto.response.CourseOutlineSearchDto;
import com.thanhdat.quanlyhoctap.dto.response.CourseOutlineTeacherResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseOutlineViewTeacherResponse;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CourseOutlineService {
    DataWithCounterDto<CourseOutlineSearchDto> search(Map<String, String> params);

    DataWithCounterDto<CourseOutlineTeacherResponse> getAllByCurrentTeacher(Map<String, String> params);

    CourseOutlineViewTeacherResponse getViewByCurrentTeacher(Long id);

    void updateByCurrentTeacher(Long id, MultipartFile file, CourseOutlineEditTeacherRequest request);
}
