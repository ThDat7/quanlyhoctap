package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.AvailableDateForMidtermExamResponse;
import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;
import com.thanhdat.quanlyhoctap.dto.response.MidtermExamResponse;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.ExamRepository;
import com.thanhdat.quanlyhoctap.repository.ScheduleStudyRepository;
import com.thanhdat.quanlyhoctap.service.ExamService;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamServiceImpl implements ExamService {
    StudentService studentService;
    CourseClassRepository courseClassRepository;
    TeacherService teacherService;
    ScheduleStudyService scheduleStudyService;
    ScheduleStudyRepository scheduleStudyRepository;
    ExamRepository examRepository;

    @Override
    public List<? extends ExamScheduleResponse> getByCurrentStudentAndSemester(Long semesterId) {
        Long currentStudentId = studentService.getCurrentStudentId();
        return courseClassRepository.findBySemesterIdAndStudentId(semesterId, currentStudentId)
                .stream()
                .flatMap(e -> convertToResponse(e).stream())
                .sorted(Comparator.comparing(ExamScheduleResponse::getStartTime))
                .toList();
    }

    @Override
    public List<MidtermExamResponse> getByCurrentTeacherAndSemester(Long semesterId) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();
        List<CourseClass> courseClasses = courseClassRepository.findBySemesterIdAndTeacherId(semesterId, currentTeacherId);
        return mapToMidtermExamResponse(courseClasses);
    }

    @Override
    public List<AvailableDateForMidtermExamResponse> getAvailableDateMidtermExam(Long courseClassId) {
        List<ScheduleStudy> scheduleStudies = scheduleStudyRepository.findByCourseClassId(courseClassId);
        return scheduleStudies.stream()
                .flatMap(e -> mapToAvailableDateForMidtermExamResponse(e).stream())
                .sorted(Comparator.comparing(AvailableDateForMidtermExamResponse::getStartTime))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMidtermExamCurrentTeacher(Long courseClassId, UpdateMidtermExamRequest updateMidtermExamRequest) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();
        validateTeacherCanUpdateMidtermExam(currentTeacherId, courseClassId);
        validateStartTimeMidtermExam(courseClassId, updateMidtermExamRequest.getStartTime());

        ScheduleStudy scheduleStudy = getMatchedStartTime(courseClassId, updateMidtermExamRequest.getStartTime()).get();

        Optional<Exam> opMidtermExam = examRepository.findByCourseClassIdAndType(courseClassId, ExamType.MIDTERM);
        Exam midtermExam = opMidtermExam.orElseGet(() -> Exam.builder()
                                                        .type(ExamType.MIDTERM)
                                                        .classroom(scheduleStudy.getClassroom())
                                                        .build());
        midtermExam.setStartTime(updateMidtermExamRequest.getStartTime());
//        need calculate end time ?
        examRepository.save(midtermExam);
    }

    private Optional<ScheduleStudy> getMatchedStartTime(Long courseClassId, LocalDateTime startTime){
        List<ScheduleStudy> scheduleStudies = scheduleStudyRepository.findByCourseClassId(courseClassId);
        return scheduleStudies.stream()
                .filter(e -> scheduleStudyService.convertToDateTimeRanges(e).stream()
                        .anyMatch(dtr -> dtr.getStart().equals(startTime)))
                .findFirst();
    }

    private void validateTeacherCanUpdateMidtermExam(Long teacherId, Long courseClassId) {
        Boolean isExistByIdAndSemesterNotLocked = courseClassRepository.existsByIdAndSemesterNotLocked(courseClassId);
        if (!isExistByIdAndSemesterNotLocked)
            throw new RuntimeException("Course class is locked.");

        Boolean isTeacherTeaching = courseClassRepository.existsByIdAndTeacherId(courseClassId, teacherId);
        if (!isTeacherTeaching)
            throw new RuntimeException("You don't teach this course class.");
    }

    private void validateStartTimeMidtermExam(Long courseClassId, LocalDateTime startTime) {
        Optional<ScheduleStudy> opMatched = getMatchedStartTime(courseClassId, startTime);

        if (opMatched.isEmpty())
            throw new RuntimeException("Start time of midterm exam must be in the schedule study times.");
    }

    private List<AvailableDateForMidtermExamResponse> mapToAvailableDateForMidtermExamResponse(ScheduleStudy scheduleStudy){
        List<DateTimeRange> dateTimeRanges = scheduleStudyService.convertToDateTimeRanges(scheduleStudy);
        RoomType roomType = scheduleStudy.getClassroom().getRoomType();
        String type = roomType == RoomType.CLASS_ROOM ? "Lý thuyết" : "Thực hành";
        return dateTimeRanges.stream()
                .map(dtr -> {
                    LocalDateTime startTime = dtr.getStart();
                    return AvailableDateForMidtermExamResponse.builder()
                            .startTime(startTime)
                            .type(type)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<ExamScheduleResponse> convertToResponse(CourseClass courseClass){
        return courseClass.getExams().stream()
                .map(exam -> ExamScheduleResponse.builder()
                .courseCode(courseClass.getCourse().getCode())
                .courseName(courseClass.getCourse().getName())
                .studentClassName(courseClass.getStudentClass().getName())
                .quantityStudent((int) courseClass.getStudies().stream().count())
                .startTime(exam.getStartTime())
                .roomName(exam.getClassroom().getName())
                .type(exam.getType())
                .build())
        .collect(Collectors.toList());
    }

    private List<MidtermExamResponse> mapToMidtermExamResponse(List<CourseClass> courseClasses){
        return courseClasses.stream()
                .map(courseClass -> {
                    Course course = courseClass.getCourse();
                    Optional<Exam> opMidtermExam = courseClass.getExams().stream()
                            .filter(e -> e.getType() == ExamType.MIDTERM)
                            .findFirst();
                    if (opMidtermExam.isEmpty())
                        return MidtermExamResponse.builder()
                                .courseClassId(courseClass.getId())
                                .courseCode(course.getCode())
                                .courseName(course.getName())
                                .build();
                    Exam midtermExam = opMidtermExam.get();
                    LocalDateTime examStartTime = midtermExam.getStartTime();
                    String roomName = midtermExam.getClassroom().getName();

                    return MidtermExamResponse.builder()
                            .courseClassId(courseClass.getId())
                            .courseCode(course.getCode())
                            .courseName(course.getName())
                            .startTime(examStartTime)
                            .roomName(roomName)
                            .build();
                })
                .collect(Collectors.toList());
    }

}
