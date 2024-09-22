package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.EducationProgramCourseDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramCourseResponse;
import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import com.thanhdat.quanlyhoctap.entity.EducationProgramCourse;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.util.SemesterCalculator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EducationProgramCourseMapper {

    @Mapping(target = "courseOutlineId", source = "courseOutline.id")
    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "courseOutlineUrl", source = "courseOutline.url")
    @Mapping(target = "semester", source = ".", qualifiedByName = "calculateSemesterInEP")
    EducationProgramCourseDto toEducationProgramCourseDto(EducationProgramCourse educationProgramCourse);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "semester", source = ".", qualifiedByName = "calculateSemesterInEP")
    @Mapping(target = "courseOutlineUrl", source = "courseOutline.url")
    @Mapping(target = "courseOutlineId", source = "courseOutline.id")
    EducationProgramCourseResponse toEducationProgramCourseResponse(EducationProgramCourse educationProgramCourse);

    @Named("calculateSemesterInEP")
    default Integer calculateSemesterInEP(EducationProgramCourse educationProgramCourse) {
        EducationProgram educationProgram = educationProgramCourse.getEducationProgram();
        Semester semester = educationProgramCourse.getSemester();
        return SemesterCalculator.calculateSemesterInEP(educationProgram, semester);
    }
}
