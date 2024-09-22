package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.EducationProgramCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramSearchDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramViewCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramViewDto;
import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {EducationProgramCourseMapper.class})
public interface EducationProgramMapper {
    @Mapping(target = "majorName", source = "major.name")
    EducationProgramSearchDto toEducationProgramSearchDto(EducationProgram educationProgram);

    @Mapping(target = "majorName", source = "major.name")
    EducationProgramViewDto toEducationProgramViewDto(EducationProgram educationProgram);

    @Mapping(target = "majorId", source = "major.id")
    EducationProgramViewCrudResponse toEducationProgramViewCrudResponse(EducationProgram educationProgram);

    @Mapping(target = "majorName", source = "major.name")
    @Mapping(target = "numberOfCourses", source = ".", qualifiedByName = "numberOfCourses")
    EducationProgramCrudResponse toEducationProgramCrudResponse(EducationProgram educationProgram);

    @Named("numberOfCourses")
    default int numberOfCourses(EducationProgram educationProgram){
        if (educationProgram.getEducationProgramCourses() == null){
            return 0;
        }
        return educationProgram.getEducationProgramCourses().size();
    }
}
