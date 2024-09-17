package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.FacultyCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.FacultyCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.FacultyViewCrudResponse;
import com.thanhdat.quanlyhoctap.entity.Faculty;
import com.thanhdat.quanlyhoctap.repository.FacultyRepository;
import com.thanhdat.quanlyhoctap.service.FacultyService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final PagingHelper pagingHelper;

    @Override
    public void delete(Integer id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public DataWithCounterDto<FacultyCrudResponse> getAll(Map params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<Faculty> page = facultyRepository.findAll(paging);
        List<FacultyCrudResponse> dto = page.getContent().stream()
                .map(this::mapToFacultyCrudResponse)
                .collect(Collectors.toList());
        Integer total = (int) page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    private FacultyCrudResponse mapToFacultyCrudResponse(Faculty faculty) {
        return FacultyCrudResponse.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .alias(faculty.getAlias())
                .build();
    }

    @Override
    public FacultyViewCrudResponse getById(Integer id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        return mapToFacultyViewCrudResponse(faculty);
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
    public void update(Integer id, FacultyCrudRequest updateRequest) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        faculty.setName(updateRequest.getName());
        faculty.setAlias(updateRequest.getAlias());
        facultyRepository.save(faculty);
    }

    private FacultyViewCrudResponse mapToFacultyViewCrudResponse(Faculty faculty) {
        return FacultyViewCrudResponse.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .alias(faculty.getAlias())
                .build();
    }

}
