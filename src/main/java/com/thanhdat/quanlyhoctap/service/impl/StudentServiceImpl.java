package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.entity.Student;
import com.thanhdat.quanlyhoctap.repository.StudentRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentServiceImpl implements StudentService {
    private static final Integer CURRENT_STUDENT_LOGGED_ID = 1;
    StudentRepository studentRepository;

    @Override
    public Integer getCurrentStudentId() {
        return CURRENT_STUDENT_LOGGED_ID;
    }

    @Override
    public Student getCurrentStudent() {
        return studentRepository.findById(CURRENT_STUDENT_LOGGED_ID).get();
    }
}
