package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.CourseCrudRequest;
import com.thanhdat.quanlyhoctap.dto.request.MajorCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.Course;
import com.thanhdat.quanlyhoctap.entity.CourseType;
import com.thanhdat.quanlyhoctap.entity.Faculty;
import com.thanhdat.quanlyhoctap.entity.Major;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.CourseRepository;
import com.thanhdat.quanlyhoctap.service.CourseService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final PagingHelper pagingHelper;

    @Override
    public void create(CourseCrudRequest createRequest) {
        Course newCourse = Course.builder()
                .name(createRequest.getName())
                .code(createRequest.getCode())
                .credits(createRequest.getCredits())
                .type(createRequest.getType())
                .sessionInWeek(createRequest.getSessionInWeek())
                .theoryPeriod(createRequest.getTheoryPeriod())
                .practicePeriod(createRequest.getPracticePeriod())
                .build();
        courseRepository.save(newCourse);
    }

    @Override
    public void delete(Integer id) {
        courseRepository.deleteById(id);
    }

    @Override
    public DataWithCounterDto<CourseCrudResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<Course> page = courseRepository.findAll(paging);
        List<CourseCrudResponse> dto = page.getContent().stream()
                .map(this::mapToCourseCrudResponse)
                .collect(Collectors.toList());
        Integer total = (int) page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    private CourseCrudResponse mapToCourseCrudResponse(Course course) {
//        majors
        return CourseCrudResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .credits(course.getCredits())
                .build();
    }

    @Override
    public CourseViewCrudResponse getById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapToCourseViewCrudResponse(course);
    }

    private CourseViewCrudResponse mapToCourseViewCrudResponse(Course course) {
        // majors
        return CourseViewCrudResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .credits(course.getCredits())
                .type(course.getType())
                .sessionInWeek(course.getSessionInWeek())
                .theoryPeriod(course.getTheoryPeriod())
                .practicePeriod(course.getPracticePeriod())
                .build();
    }

    @Override
    public void update(Integer id, CourseCrudRequest updateRequest) {
        Course oldCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
//        validate
        oldCourse.setName(updateRequest.getName());
        oldCourse.setCode(updateRequest.getCode());
        oldCourse.setCredits(updateRequest.getCredits());
        oldCourse.setType(updateRequest.getType());
        oldCourse.setSessionInWeek(updateRequest.getSessionInWeek());
        oldCourse.setTheoryPeriod(updateRequest.getTheoryPeriod());
        oldCourse.setPracticePeriod(updateRequest.getPracticePeriod());
        courseRepository.save(oldCourse);
    }

    @Override
    public List<SelectOptionResponse> getTypes() {
        List<CourseType> types = List.of(CourseType.values());
        return types.stream()
                .map(type -> mapToSelectOptionResponse(type, type.toString()))
                .collect(Collectors.toList());
    }

    private SelectOptionResponse mapToSelectOptionResponse(Object value, String label) {
        return SelectOptionResponse.builder()
                .value(value)
                .label(label)
                .build();
    }

}
