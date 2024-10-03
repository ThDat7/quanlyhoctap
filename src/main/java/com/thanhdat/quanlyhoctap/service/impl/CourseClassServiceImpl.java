package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.mapper.CourseClassMapper;
import com.thanhdat.quanlyhoctap.repository.*;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClassServiceImpl implements CourseClassService {
    CourseClassRepository courseClassRepository;
    TeacherService teacherService;

    PagingHelper pagingHelper;

    CourseClassMapper courseClassMapper;

    @Override
    public List<TeacherCourseClassTeachingResponse> getCurrentTeacherTeaching(Long semesterId) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();

        List<CourseClass> courseClasses = courseClassRepository.findByTeacherIdAndSemesterId(currentTeacherId, semesterId);
        return courseClasses.stream()
                .map(courseClassMapper::toTeacherCourseClassTeachingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DataWithCounterDto<CourseClassResponse> getCourseClassBySemesterAndCourse(Long semesterId, Long courseId, Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<CourseClass> page = courseClassRepository.findBySemesterIdAndCourseId(semesterId, courseId, paging);
        List<CourseClass> data = page.getContent();
        List<CourseClassResponse> courseClassResponses = data.stream()
                .map(courseClassMapper::toCourseClassResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(courseClassResponses, total);
    }
}
