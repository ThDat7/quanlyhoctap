package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.FacultyCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.Faculty;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.FacultyMapper;
import com.thanhdat.quanlyhoctap.mapper.UtilMapper;
import com.thanhdat.quanlyhoctap.repository.FacultyRepository;
import com.thanhdat.quanlyhoctap.service.FacultyService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FacultyServiceImpl implements FacultyService {
    FacultyRepository facultyRepository;

    PagingHelper pagingHelper;

    FacultyMapper facultyMapper;
    UtilMapper utilMapper;

    @Override
    public List<SelectOptionResponse> getAllForSelect() {
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties.stream()
                .map(faculty -> utilMapper.toSelectOptionResponse(faculty.getId(), faculty.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new AppException(ErrorCode.FACULTY_NOT_FOUND);
        }

        facultyRepository.deleteById(id);
    }

    @Override
    public DataWithCounterDto<FacultyCrudResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<Faculty> page = facultyRepository.findAll(paging);
        List<FacultyCrudResponse> dto = page.getContent().stream()
                .map(facultyMapper::toFacultyCrudResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    @Override
    public FacultyViewCrudResponse getById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_FOUND));
        return facultyMapper.toFacultyViewCrudResponse(faculty);
    }
    @Override
    public void create(FacultyCrudRequest createRequest) {
        Faculty faculty = Faculty.builder()
                .name(createRequest.getName())
                .alias(createRequest.getAlias())
                .build();
        facultyRepository.save(faculty);
    }

    @Override
    public void update(Long id, FacultyCrudRequest updateRequest) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_FOUND));
        faculty.setName(updateRequest.getName());
        faculty.setAlias(updateRequest.getAlias());
        facultyRepository.save(faculty);
    }

}
