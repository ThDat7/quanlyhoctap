package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.CourseCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.Course;
import com.thanhdat.quanlyhoctap.entity.CourseType;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.CourseMapper;
import com.thanhdat.quanlyhoctap.mapper.UtilMapper;
import com.thanhdat.quanlyhoctap.repository.CourseRepository;
import com.thanhdat.quanlyhoctap.service.CourseService;
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
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;

    PagingHelper pagingHelper;

    CourseMapper courseMapper;
    UtilMapper utilMapper;

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
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        courseRepository.deleteById(id);
    }

    @Override
    public DataWithCounterDto<CourseCrudResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<Course> page = courseRepository.findAll(paging);
        List<CourseCrudResponse> dto = page.getContent().stream()
                .map(courseMapper::toCourseCrudResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    @Override
    public CourseViewCrudResponse getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseMapper.toCourseViewCrudResponse(course);
    }

    @Override
    public void update(Long id, CourseCrudRequest updateRequest) {
        Course oldCourse = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
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
                .map(type -> utilMapper.toSelectOptionResponse(type, type.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectOptionResponse> getSelectOptions() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> utilMapper.toSelectOptionResponse(course.getId(), course.getName()))
                .collect(Collectors.toList());
    }

}
