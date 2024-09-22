package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.CourseOutline;
import com.thanhdat.quanlyhoctap.entity.EducationProgramCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CourseRuleMapper.class})
public interface CourseOutlineMapper {

    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "courseCode", source = "course.code")
    CourseOutlineTeacherResponse toCourseOutlineTeacherResponse(CourseOutline courseOutline);

    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "teacherName", source = "teacher.fullName")
    @Mapping(target = "courseCredits", source = "course.credits")
    @Mapping(target = "years", source = "educationProgramCourses", qualifiedByName = "schoolYears")
    CourseOutlineSearchDto toCourseOutlineSearchDto(CourseOutline courseOutline);

    @Named("schoolYears")
    default List<Integer> mapEducationProgramCoursesToSchoolYears(Set<EducationProgramCourse> educationProgramCourses) {
        return educationProgramCourses.stream()
                .map(epc -> epc.getEducationProgram().getSchoolYear())
                .collect(Collectors.toList());
    }

    @Mapping(target = "courseName", source = "course.name")
    @Mapping(target = "courseCode", source = "course.code")
    CourseOutlineViewTeacherResponse toCourseOutlineViewTeacherResponse(CourseOutline courseOutline);
}
