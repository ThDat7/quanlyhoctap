package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.entity.Exam;
import com.thanhdat.quanlyhoctap.entity.RoomType;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ExamMapper {
    @Mapping(target = "courseClassId", source = "courseClass.id")
    @Mapping(target = "courseCode", source = "courseClass.course.code")
    @Mapping(target = "courseName", source = "courseClass.course.name")
    @Mapping(target = "startTime", source = "midtermExam.startTime")
    @Mapping(target = "roomName", source = "midtermExam.classroom.name")
    public abstract MidtermExamResponse toMidtermExamResponse(CourseClass courseClass, Exam midtermExam);

    @Mapping(target = "courseCode", source = "courseClass.course.code")
    @Mapping(target = "courseName", source = "courseClass.course.name")
    @Mapping(target = "studentClassName", source = "courseClass.studentClass.name")
    @Mapping(target = "startTime", source = "exam.startTime")
    @Mapping(target = "roomName", source = "exam.classroom.name")
    @Mapping(target = "type", source = "exam.type")
    protected abstract ExamScheduleResponse toExamScheduleResponse(CourseClass courseClass, Exam exam,
                                                                   int quantityStudent);

    public List<ExamScheduleResponse> toExamScheduleResponseList(CourseClass courseClass) {
        int quantityStudent = courseClass.getStudies() != null ? courseClass.getStudies().size() : 0;

        if (courseClass.getExams() == null) {
            return List.of();
        }

        return courseClass.getExams().stream()
                .map(exam -> toExamScheduleResponse(courseClass, exam, quantityStudent))
                .toList();
    }

    @Mapping(target = "startTime", source = "dateTimeRange.start")
    @Mapping(target = "type", source = "roomType.description")
    protected abstract AvailableDateForMidtermExamResponse toAvailableDateForMidtermExamResponse(
            DateTimeRange dateTimeRange, RoomType roomType);

    public List<AvailableDateForMidtermExamResponse> toAvailableDateForMidtermExamResponseList(
            List<DateTimeRange> dateTimeRanges, RoomType roomType) {
        if (dateTimeRanges == null) {
            return List.of();
        }

        return dateTimeRanges.stream()
                .map(dateTimeRange -> toAvailableDateForMidtermExamResponse(dateTimeRange, roomType))
                .toList();
    }

    @Mapping(target = "courseClassId", source = "courseClass.id")
    @Mapping(target = "courseName", source = "courseClass.course.name")
    @Mapping(target = "courseCode", source = "courseClass.course.code")
    @Mapping(target = "startTime", source = "finalExam.startTime")
    @Mapping(target = "roomName", source = "finalExam.classroom.name")
    public abstract FinalExamResponse toFinalExamResponse(CourseClass courseClass, Exam finalExam);

    @Mapping(target = "id", source = "courseId")
    @Mapping(target = "code", source = "courseCode")
    @Mapping(target = "name", source = "courseName")
    public abstract CourseWithFinalExamScheduleStatusResponse toCourseWithFinalExamScheduleStatus(Long courseId,
                                                                                                  String courseCode,
                                                                                                  String courseName,
                                                                                                  Boolean isHaveFullFinalExamSchedule);

    public AvailableTimeForFinalExamResponse toAvailableTimeForFinalExamResponse(List<LocalDateTime> startTimeSlots) {
        return AvailableTimeForFinalExamResponse.builder()
                .startTimeSlots(startTimeSlots)
                .build();
    }

}
