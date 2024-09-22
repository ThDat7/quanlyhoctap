package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.InvoiceResponse;
import com.thanhdat.quanlyhoctap.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "courseCode", source = "course.code")
    @Mapping(target = "courseCredits", source = "course.credits")
    InvoiceResponse toInvoiceResponse(Course course, Integer tuition);
}
