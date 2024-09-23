package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.entity.Student;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.repository.StudentRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentServiceImpl implements StudentService {
    StudentRepository studentRepository;
    UserService userService;

    @Override
    public Long getCurrentStudentId() {
        return getCurrentStudent().getId();
    }

    @Override
    public Student getCurrentStudent() {
        long userId = userService.getCurrentUserId();
        return studentRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHORIZED));
    }
}
