package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.entity.Teacher;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.repository.TeacherRepository;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import com.thanhdat.quanlyhoctap.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherServiceImpl implements TeacherService {
    UserService userService;
    TeacherRepository teacherRepository;

    public Teacher getCurrentTeacher() {
        long userId = userService.getCurrentUserId();
        return teacherRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHORIZED));
    }

    public Long getCurrentTeacherId() {
        return getCurrentTeacher().getId();
    }
}
