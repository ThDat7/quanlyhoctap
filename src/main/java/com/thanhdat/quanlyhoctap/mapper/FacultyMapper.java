package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.FacultyCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.FacultyViewCrudResponse;
import com.thanhdat.quanlyhoctap.entity.Faculty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyViewCrudResponse toFacultyViewCrudResponse(Faculty faculty);
    FacultyCrudResponse toFacultyCrudResponse(Faculty faculty);
}
