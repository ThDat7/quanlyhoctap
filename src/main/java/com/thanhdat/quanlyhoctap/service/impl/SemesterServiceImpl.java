package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.dto.response.SemesterDetailResponse;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.repository.SemesterRepository;
import com.thanhdat.quanlyhoctap.service.SemesterService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterServiceImpl implements SemesterService {
    SemesterRepository semesterRepository;
    StudentService studentService;
    TeacherService teacherService;

    @Override
    public List<SemesterDetailResponse> getByCurrentStudent() {
        Integer currentStudentId = studentService.getCurrentStudentId();
        return semesterRepository.findByStudentId(currentStudentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SemesterDetailResponse> getByCurrentTeacher() {
        Integer currentTeacherId = teacherService.getCurrentTeacherId();
        return semesterRepository.findByTeacherId(currentTeacherId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SemesterDetailResponse> getNoneLocked() {
        LocalDateTime now = LocalDateTime.now();
        List<Semester> noneLocked = semesterRepository.findByLockTimeGreaterThan(now);
        return noneLocked.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SemesterDetailResponse convertToResponse(Semester semester){
        String year = String.format("%d-%d", semester.getYear(), semester.getYear() + 1);
        SemesterDetailResponse semesterDetailResponse = SemesterDetailResponse.builder()
                .id(semester.getId())
                .semester(semester.getSemester())
                .year(year)
                .startDate(semester.getStartDate())
                .durationWeeks(semester.getDurationWeeks())
                .build();
        return semesterDetailResponse;
    }

}
