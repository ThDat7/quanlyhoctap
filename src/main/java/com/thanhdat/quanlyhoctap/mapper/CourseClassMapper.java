package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.entity.Semester;
import com.thanhdat.quanlyhoctap.entity.Study;
import com.thanhdat.quanlyhoctap.util.YearHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = {ScheduleStudyMapper.class, StudyMapper.class})
public abstract class CourseClassMapper {
    @Autowired
    private StudyMapper studyMapper;

    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source = "course.code", target = "courseCode")
    public abstract TeacherCourseClassTeachingResponse toTeacherCourseClassTeachingResponse(CourseClass courseClass);

    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source = "course.code", target = "courseCode")
    @Mapping(source = "course.credits", target = "courseCredits")
    @Mapping(source = "studentClass.name", target = "studentClassName")
    @Mapping(source = "teacher.fullName", target = "teacherName")
    public abstract CourseClassResponse toCourseClassResponse(CourseClass courseClass);

    @Mapping(source = "courseClass.id", target = "id")
    @Mapping(source = "courseClass.course.code", target = "courseCode")
    @Mapping(source = "courseClass.course.name", target = "courseName")
    @Mapping(source = "courseClass.course.credits", target = "courseCredits")
    @Mapping(source = "courseClass.studentClass.name", target = "studentClassName")
    @Mapping(source = "courseClass.capacity", target = "capacity")
    @Mapping(source = "remainingSlot", target = "remaining")
    @Mapping(source = "courseClass.teacher.teacherCode", target = "teacherCode")
    @Mapping(source = "courseClass.scheduleStudies", target = "scheduleStudies")
    public abstract CourseRegisterOpenResponse toCourseRegisterOpenResponse(CourseClass courseClass, int remainingSlot);

    public StudentCourseRegisterResponse toStudentCourseRegisterResponse(Semester semesterRegister,
                                                                  Map<CourseClass, Integer> openWithRemaining,
                                                                  List<Study> registered) {
        String year = YearHelper.getYearStringBySemester(semesterRegister);
        List<CourseRegisterOpenResponse> openCourses = openWithRemaining.entrySet().stream()
                .map(entry -> toCourseRegisterOpenResponse(entry.getKey(), entry.getValue()))
                .toList();
        List<CourseRegisteredResponse> registeredCourse = registered.stream()
                .map(studyMapper::toCourseRegisteredResponse)
                .toList();
        return StudentCourseRegisterResponse.builder()
                .semester(semesterRegister.getSemester())
                .year(year)
                .openCourses(openCourses)
                .registeredCourses(registeredCourse)
                .build();
    }

    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source = "course.code", target = "courseCode")
    @Mapping(source = "course.credits", target = "courseCredits")
    @Mapping(source = "studentClass.name", target = "studentClassName")
    @Mapping(source = "teacher.fullName", target = "teacherName")
    @Mapping(source = "scheduleStudies", target = "schedules")
    public abstract CourseClassScheduleResponse toCourseClassScheduleResponse(CourseClass courseClass);

    public abstract CourseWithCourseClassCountResponse toCourseWithCourseClassCountResponse(Long id, String name, Float credit, Long needCount, Long presentCount);


    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "studentClassId", source = "studentClass.id")
    @Mapping(target = "semesterId", source = "semester.id")
    @Mapping(target = "courseId", source = "course.id")
    public abstract CourseClassViewCrudResponse toCourseClassViewCrudResponse(CourseClass courseClass);

    @Mapping(target = "courseName", source = "courseClass.course.name")
    public abstract CourseClassWithStatusResponse toCourseClassWithStatusResponse(CourseClass courseClass, boolean status);
}
