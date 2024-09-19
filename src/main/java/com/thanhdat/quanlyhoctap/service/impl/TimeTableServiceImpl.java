package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.CourseClassScheduleResponse;
import com.thanhdat.quanlyhoctap.dto.response.ScheduleStudyTimeTableResponse;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.SemesterRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import com.thanhdat.quanlyhoctap.service.TimeTableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableServiceImpl implements TimeTableService {
    CourseClassRepository courseClassRepository;
    SemesterRepository semesterRepository;
    StudentService studentService;
    TeacherService teacherService;
    @Override
    public List<CourseClassScheduleResponse> getByCurrentStudentAndSemester(Integer semesterId) {
        int currentStudentId = studentService.getCurrentStudentId();
        return courseClassRepository.findBySemesterIdAndStudentId(semesterId, currentStudentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseClassScheduleResponse> getByCurrentTeacherAndSemester(Integer semesterId) {
        int currentStudentId = teacherService.getCurrentTeacherId();
        return courseClassRepository.findBySemesterIdAndTeacherId(semesterId, currentStudentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private CourseClassScheduleResponse convertToResponse(CourseClass courseClass){
        List<ScheduleStudyTimeTableResponse> schedules = courseClass.getScheduleStudies().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return CourseClassScheduleResponse.builder()
                .id(courseClass.getId())
                .courseName(courseClass.getCourse().getName())
                .courseCode(courseClass.getCourse().getCode())
                .courseCredits(courseClass.getCourse().getCredits())
                .studentClassName(courseClass.getStudentClass().getName())
                .teacherName(courseClass.getTeacher().getFullName())
                .schedules(schedules)
                .build();
    }

    private ScheduleStudyTimeTableResponse convertToResponse(ScheduleStudy scheduleStudy) {
        return ScheduleStudyTimeTableResponse.builder()
                .startDate(scheduleStudy.getStartDate())
                .weekLength(scheduleStudy.getWeekLength())
                .shiftStart(scheduleStudy.getShiftStart())
                .shiftLength(scheduleStudy.getShiftLength())
                .roomType(scheduleStudy.getClassroom().getRoomType().name())
                .roomName(scheduleStudy.getClassroom().getName())
                .build();
    }
}
