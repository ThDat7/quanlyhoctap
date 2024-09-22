package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.CourseRegisteredResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudyResultCourseResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudyResultSemesterResponse;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.entity.Study;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ScheduleStudyMapper.class})
public interface StudyMapper {
    @Mapping(source = "courseClass.id", target = "id")
    @Mapping(source = "courseClass.course.code", target = "courseCode")
    @Mapping(source = "courseClass.course.name", target = "courseName")
    @Mapping(source = "courseClass.course.credits", target = "courseCredits")
    @Mapping(source = "courseClass.studentClass.name", target = "studentClassName")
    @Mapping(source = "courseClass.teacher.teacherCode", target = "teacherCode")
    @Mapping(source = "courseClass.scheduleStudies", target = "scheduleStudies")
    CourseRegisteredResponse toCourseRegisteredResponse(Study study);

    @Mapping(source = "courseClass.course.code", target = "courseCode")
    @Mapping(source = "courseClass.course.name", target = "courseName")
    @Mapping(source = "courseClass.course.credits", target = "credits")
    @Mapping(source = "courseClass.studentClass.name", target = "studentClassName")
    StudyResultCourseResponse toStudyResultCourseResponse(CourseClass courseClass,
                                                          Float midTermScore, Float finalTermScore,
                                                          Float totalScore10, Float totalScore4,
                                                          String totalScoreLetter, Boolean isPassed);

    @Mapping(source = "semester.semester", target = "semester")
    @Mapping(source = "semester.year", target = "year")
    @Mapping(source = "resultCourses", target = "courses")
    StudyResultSemesterResponse toStudyResultSemesterResponse(Semester semester,
                                                              Float creditsEarnedSemester, Float GPA4Semester,
                                                              List<StudyResultCourseResponse> resultCourses);

}
