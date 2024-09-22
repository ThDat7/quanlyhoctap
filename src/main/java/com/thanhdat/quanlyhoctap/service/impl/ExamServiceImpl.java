package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.mapper.ExamMapper;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.ExamRepository;
import com.thanhdat.quanlyhoctap.repository.ScheduleStudyRepository;
import com.thanhdat.quanlyhoctap.service.ExamService;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
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
    ScheduleStudyRepository scheduleStudyRepository;
    ExamRepository examRepository;

    StudentService studentService;
    CourseClassRepository courseClassRepository;
    TeacherService teacherService;
    ScheduleStudyService scheduleStudyService;

    PagingHelper pagingHelper;

    ExamMapper examMapper;

    @Override
    public List<ExamScheduleResponse> getByCurrentStudentAndSemester(Long semesterId) {
        Long currentStudentId = studentService.getCurrentStudentId();
        return courseClassRepository.findBySemesterIdAndStudentId(semesterId, currentStudentId)
                .stream()
                .flatMap(e -> examMapper.toExamScheduleResponseList(e).stream())
                .sorted(Comparator.comparing(ExamScheduleResponse::getStartTime))
                .toList();
    }

    @Override
    public List<MidtermExamResponse> getByCurrentTeacherAndSemester(Long semesterId) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();
        List<CourseClass> courseClasses = courseClassRepository.findBySemesterIdAndTeacherId(semesterId, currentTeacherId);
        return courseClasses.stream()
                .map(courseClass -> {
                    Exam midtermExam = courseClass.getExams().stream()
                            .filter(e -> e.getType() == ExamType.MIDTERM)
                            .findFirst().get();
                    return examMapper.toMidtermExamResponse(courseClass, midtermExam);
                }).collect(Collectors.toList());
    }

    @Override
    public List<AvailableDateForMidtermExamResponse> getAvailableDateMidtermExam(Long courseClassId) {
        List<ScheduleStudy> scheduleStudies = scheduleStudyRepository.findByCourseClassId(courseClassId);
        return scheduleStudies.stream()
                .flatMap(scheduleStudy -> {
                    List<DateTimeRange> dateTimeRanges = scheduleStudyService.convertToDateTimeRanges(scheduleStudy);
                    RoomType roomType = scheduleStudy.getClassroom().getRoomType();
                    return examMapper.toAvailableDateForMidtermExamResponseList(dateTimeRanges, roomType).stream();
                })
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
}
