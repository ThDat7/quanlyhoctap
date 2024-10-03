package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.StudentClassWithStatusResponse;
import com.thanhdat.quanlyhoctap.entity.StudentClass;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentClassMapper {
    StudentClassWithStatusResponse toStudentClassWithStatusResponse(StudentClass studentClass, boolean status);
}
