package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.service.TeacherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherServiceImpl implements TeacherService {
    private static final Long CURRENT_TEACHER_LOGGED_ID = (long) 2;

    public Long getCurrentTeacherId() {
        return CURRENT_TEACHER_LOGGED_ID;
    }
}
