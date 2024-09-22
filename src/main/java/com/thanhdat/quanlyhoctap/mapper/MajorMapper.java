package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.MajorCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.MajorViewCrudResponse;
import com.thanhdat.quanlyhoctap.entity.Major;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MajorMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorViewCrudResponse toMajorViewCrudResponse(Major major);

    @Mapping(source = "faculty.name", target = "faculty")
    MajorCrudResponse toMajorCrudResponse(Major major);
}
