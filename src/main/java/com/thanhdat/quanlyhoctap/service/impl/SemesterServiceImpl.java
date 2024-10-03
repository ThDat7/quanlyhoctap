package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.SemesterDetailResponse;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.mapper.SemesterMapper;
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

    SemesterMapper semesterMapper;

    @Override
    public List<SemesterDetailResponse> getByCurrentStudent() {
        Long currentStudentId = studentService.getCurrentStudentId();
        return semesterRepository.findByStudentId(currentStudentId).stream()
                .map(semesterMapper::toSemesterDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SemesterDetailResponse> getByCurrentTeacher() {
        Long currentTeacherId = teacherService.getCurrentTeacherId();
        return semesterRepository.findByTeacherId(currentTeacherId).stream()
                .map(semesterMapper::toSemesterDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SemesterDetailResponse> getNoneLocked() {
        LocalDateTime now = LocalDateTime.now();
        List<Semester> noneLocked = semesterRepository.findByLockTimeGreaterThan(now);
        return noneLocked.stream()
                .map(semesterMapper::toSemesterDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SemesterDetailResponse> getAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "year", "semester");
        List<Semester> semesters = semesterRepository.findAll(sort);
        return semesters.stream()
                .map(semesterMapper::toSemesterDetailResponse)
                .collect(Collectors.toList());
    }
}
