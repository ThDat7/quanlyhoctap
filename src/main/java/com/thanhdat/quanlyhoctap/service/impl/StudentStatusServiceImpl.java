package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.StudentStatusResponse;
import com.thanhdat.quanlyhoctap.entity.StudentStatus;
import com.thanhdat.quanlyhoctap.mapper.StudentStatusMapper;
import com.thanhdat.quanlyhoctap.repository.StudentStatusRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.StudentStatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentStatusServiceImpl implements StudentStatusService {
    StudentStatusRepository studentStatusRepository;

    StudentService studentService;

    StudentStatusMapper studentStatusMapper;

    public List<StudentStatusResponse> getByCurrentStudent() {
        Long currentStudentId = studentService.getCurrentStudentId();
        List<StudentStatus> studentStatuses =  studentStatusRepository.findByStudentId(currentStudentId);

        return studentStatuses.stream()
                .map(studentStatusMapper::toStudentStatusResponse)
                .collect(Collectors.toList());
    }
}
