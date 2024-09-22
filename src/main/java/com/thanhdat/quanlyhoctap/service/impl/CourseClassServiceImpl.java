package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.TeacherCourseClassTeachingResponse;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.mapper.CourseClassMapper;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClassServiceImpl implements CourseClassService {
    CourseClassRepository courseClassRepository;
    TeacherService teacherService;

    CourseClassMapper courseClassMapper;

    @Override
    public List<TeacherCourseClassTeachingResponse> getCurrentTeacherTeaching(Long semesterId) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();

        List<CourseClass> courseClasses = courseClassRepository.findByTeacherIdAndSemesterId(currentTeacherId, semesterId);
        return courseClasses.stream()
                .map(courseClassMapper::toTeacherCourseClassTeachingResponse)
                .collect(Collectors.toList());
    }
}
