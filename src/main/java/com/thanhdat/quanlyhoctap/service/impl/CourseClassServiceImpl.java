package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.TeacherCourseClassTeachingResponse;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseClassServiceImpl implements CourseClassService {
    private final CourseClassRepository courseClassRepository;
    private final TeacherService teacherService;

    @Override
    public List<TeacherCourseClassTeachingResponse> getCurrentTeacherTeaching(Integer semesterId) {
        Integer currentTeacherId = teacherService.getCurrentTeacherId();

        List<CourseClass> courseClasses = courseClassRepository.findByTeacherIdAndSemesterId(currentTeacherId, semesterId);
        return mapToTeacherCourseClassTeachingResponse(courseClasses);
    }

    private List<TeacherCourseClassTeachingResponse> mapToTeacherCourseClassTeachingResponse(List<CourseClass> courseClasses) {
        return courseClasses.stream()
                .map(cc -> TeacherCourseClassTeachingResponse.builder()
                            .id(cc.getId())
                            .courseName(cc.getCourse().getName())
                            .courseCode(cc.getCourse().getCode())
                            .build())
                .collect(Collectors.toList());
    }
}
