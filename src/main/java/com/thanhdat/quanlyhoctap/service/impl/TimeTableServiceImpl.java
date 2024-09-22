package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.CourseClassScheduleResponse;
import com.thanhdat.quanlyhoctap.mapper.CourseClassMapper;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import com.thanhdat.quanlyhoctap.service.TimeTableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableServiceImpl implements TimeTableService {
    CourseClassRepository courseClassRepository;
    StudentService studentService;
    TeacherService teacherService;

    CourseClassMapper courseClassMapper;

    @Override
    public List<CourseClassScheduleResponse> getByCurrentStudentAndSemester(Long semesterId) {
        Long currentStudentId = studentService.getCurrentStudentId();
        return courseClassRepository.findBySemesterIdAndStudentId(semesterId, currentStudentId).stream()
                .map(courseClassMapper::toCourseClassScheduleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseClassScheduleResponse> getByCurrentTeacherAndSemester(Long semesterId) {
        Long currentStudentId = teacherService.getCurrentTeacherId();
        return courseClassRepository.findBySemesterIdAndTeacherId(semesterId, currentStudentId).stream()
                .map(courseClassMapper::toCourseClassScheduleResponse)
                .collect(Collectors.toList());
    }
}
