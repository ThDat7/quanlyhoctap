package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.SemesterResponse;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.repository.SemesterRepository;
import com.thanhdat.quanlyhoctap.service.SemesterService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SemesterServiceImpl implements SemesterService {
    private final SemesterRepository semesterRepository;
    private final StudentService studentService;

    @Override
    public List<SemesterResponse> getByCurrentStudent() {
        Integer currentStudentId = studentService.getCurrentStudentId();
        return semesterRepository.findByStudentId(currentStudentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SemesterResponse convertToResponse(Semester semester){
        String year = String.format("%d-%d", semester.getYear(), semester.getYear() + 1);
        SemesterResponse semesterResponse = SemesterResponse.builder()
                .id(semester.getId())
                .semester(semester.getSemester())
                .year(year)
                .startDate(semester.getStartDate())
                .durationWeeks(semester.getDurationWeeks())
                .build();
        return semesterResponse;
    }

}
