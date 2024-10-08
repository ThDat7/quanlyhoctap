package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.MajorCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.MajorCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.MajorViewCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.entity.Faculty;
import com.thanhdat.quanlyhoctap.entity.Major;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.MajorMapper;
import com.thanhdat.quanlyhoctap.mapper.UtilMapper;
import com.thanhdat.quanlyhoctap.repository.FacultyRepository;
import com.thanhdat.quanlyhoctap.repository.MajorRepository;
import com.thanhdat.quanlyhoctap.service.MajorService;
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
public class MajorServiceImpl implements MajorService {
    MajorRepository majorRepository;
    FacultyRepository facultyRepository;
    
    PagingHelper pagingHelper;

    UtilMapper utilMapper;
    MajorMapper majorMapper;

    public DataWithCounterDto<MajorCrudResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
            Page<Major> page = majorRepository.findAll(paging);
        List<MajorCrudResponse> dto = page.getContent().stream()
                .map(majorMapper::toMajorCrudResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    @Override
    public void create(MajorCrudRequest majorCrudRequest) {
        Faculty faculty = facultyRepository.findById(majorCrudRequest.getFacultyId())
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_FOUND));
        Major newMajor = Major.builder()
                .name(majorCrudRequest.getName())
                .alias(majorCrudRequest.getAlias())
                .specializeTuition(majorCrudRequest.getSpecializeTuition())
                .generalTuition(majorCrudRequest.getGeneralTuition())
                .faculty(faculty)
                .build();
        majorRepository.save(newMajor);
    }

    @Override
    public void update(Long id, MajorCrudRequest majorCrudRequest) {
        Major oldMajor = majorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
//        validate
        oldMajor.setName(majorCrudRequest.getName());
        oldMajor.setAlias(majorCrudRequest.getAlias());
        oldMajor.setSpecializeTuition(majorCrudRequest.getSpecializeTuition());
        oldMajor.setGeneralTuition(majorCrudRequest.getGeneralTuition());

        Boolean isFacultyChanged = !oldMajor.getFaculty().getId().equals(majorCrudRequest.getFacultyId());
        if (isFacultyChanged) {
            Faculty newFaculty = facultyRepository.findById(majorCrudRequest.getFacultyId())
                    .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_FOUND));
            oldMajor.setFaculty(newFaculty);
        }
        majorRepository.save(oldMajor);
    }

    @Override
    public void delete(Long id) {
        if (!majorRepository.existsById(id)) {
            throw new AppException(ErrorCode.MAJOR_NOT_FOUND);
        }

        majorRepository.deleteById(id);
    }

    @Override
    public MajorViewCrudResponse getById(Long id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
        return majorMapper.toMajorViewCrudResponse(major);
    }

    @Override
    public List<SelectOptionResponse> getSelectOptions() {
        List<Major> majors = majorRepository.findAll();
        return majors.stream()
                .map(major -> utilMapper.toSelectOptionResponse(major.getId(), major.getName()))
                .collect(Collectors.toList());
    }
}
