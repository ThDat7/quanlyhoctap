package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.StudentStatusResponse;
import com.thanhdat.quanlyhoctap.entity.StudentStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SemesterMapper.class})
public interface StudentStatusMapper {
    StudentStatusResponse toStudentStatusResponse(StudentStatus studentStatus);
}
