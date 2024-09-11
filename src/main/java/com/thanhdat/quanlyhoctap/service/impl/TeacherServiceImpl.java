package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private static final Integer CURRENT_TEACHER_LOGGED_ID = 2;

    public Integer getCurrentTeacherId() {
        return CURRENT_TEACHER_LOGGED_ID;
    }
}
