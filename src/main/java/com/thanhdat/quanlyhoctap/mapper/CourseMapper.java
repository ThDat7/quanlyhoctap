package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.CourseCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseViewCrudResponse;
import com.thanhdat.quanlyhoctap.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseResponse toCourseResponse(Course course);
    CourseViewCrudResponse toCourseViewCrudResponse(Course course);
    CourseCrudResponse toCourseCrudResponse(Course course);
}
