package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.ClassroomResponse;
import com.thanhdat.quanlyhoctap.entity.Classroom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {
    ClassroomResponse toClassroomResponse(Classroom classroom);
}
