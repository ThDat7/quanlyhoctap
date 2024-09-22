package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.SemesterDetailResponse;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.util.YearHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class SemesterMapper {
    @Mapping(target = "year", source = ".", qualifiedByName = "getYearStringBySemester")
    public abstract SemesterDetailResponse toSemesterDetailResponse(Semester semester);

    @Named("getYearStringBySemester")
    protected String getYearStringBySemester(Semester semester){
        return YearHelper.getYearStringBySemester(semester);
    }
}
