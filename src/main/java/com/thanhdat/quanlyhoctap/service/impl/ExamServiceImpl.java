package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.GetRoomAvailableFinalExamRequest;
import com.thanhdat.quanlyhoctap.dto.request.UpdateFinalExamRequest;
import com.thanhdat.quanlyhoctap.dto.request.UpdateMidtermExamRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.ClassroomMapper;
import com.thanhdat.quanlyhoctap.mapper.ExamMapper;
import com.thanhdat.quanlyhoctap.repository.ClassroomRepository;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.ExamRepository;
import com.thanhdat.quanlyhoctap.repository.ScheduleStudyRepository;
import com.thanhdat.quanlyhoctap.service.*;
import com.thanhdat.quanlyhoctap.specification.CourseClassSpecification;
import com.thanhdat.quanlyhoctap.util.DateRange;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamServiceImpl implements ExamService {
    ScheduleStudyRepository scheduleStudyRepository;
    ExamRepository examRepository;
    CourseClassRepository courseClassRepository;
    ClassroomRepository classroomRepository;

    StudentService studentService;
    TeacherService teacherService;
    ScheduleStudyService scheduleStudyService;
    ClassroomService classroomService;

    PagingHelper pagingHelper;

    ExamMapper examMapper;
    ClassroomMapper classroomMapper;

    @Override
    public List<ExamScheduleResponse> getByCurrentStudentAndSemester(Long semesterId) {
        Long currentStudentId = studentService.getCurrentStudentId();
        return courseClassRepository.findBySemesterIdAndStudentId(semesterId, currentStudentId)
                .stream()
                .flatMap(e -> examMapper.toExamScheduleResponseList(e).stream())
                .sorted(Comparator.comparing(ExamScheduleResponse::getStartTime))
                .toList();
    }

//    need check again if exam not exist, teacher can request course class id to create it
    @Override
    public List<MidtermExamResponse> getByCurrentTeacherAndSemester(Long semesterId) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();
        List<CourseClass> courseClasses = courseClassRepository.findBySemesterIdAndTeacherId(semesterId, currentTeacherId);
        return courseClasses.stream()
                .map(courseClass -> {
                    Exam midtermExam = courseClass.getExams().stream()
                            .filter(e -> e.getType() == ExamType.MIDTERM)
                            .findFirst().orElseGet(() -> Exam.builder().build());
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

        ScheduleStudy scheduleStudy = getMatchedStartTime(courseClassId, updateMidtermExamRequest.getStartTime())
                .orElseThrow(() ->
                        new AppException(ErrorCode.MIDTERM_EXAM_START_TIME_NOT_IN_STUDY_TIMES));

        Optional<Exam> opMidtermExam = examRepository.findByCourseClassIdAndType(courseClassId, ExamType.MIDTERM);
        Exam midtermExam = opMidtermExam.orElseGet(() -> Exam.builder()
                                                        .type(ExamType.MIDTERM)
                                                        .classroom(scheduleStudy.getClassroom())
                                                        .build());
        midtermExam.setStartTime(updateMidtermExamRequest.getStartTime());
//        need calculate end time ?
        examRepository.save(midtermExam);
    }

    @Override
    public List<CourseWithFinalExamScheduleStatusResponse> getCourseWithFinalExamScheduleStatus(Long semesterId) {
        ExamType finalType = ExamType.FINAL;
        List<Object[]> courseClasses = examRepository.findCourseWithFinalExamStatus(semesterId, finalType);
        return courseClasses.stream()
                .map(this::queryResultToCourseWithFinalExamScheduleStatus)
                .collect(Collectors.toList());
    }

    private CourseWithFinalExamScheduleStatusResponse queryResultToCourseWithFinalExamScheduleStatus(Object[] objects){
        Long courseId = (Long) objects[0];
        String courseCode = (String) objects[1];
        String courseName = (String) objects[2];
        Boolean isHaveFullFinalExamSchedule = (Boolean) objects[3];

        return examMapper.toCourseWithFinalExamScheduleStatus(courseId, courseCode, courseName,
                isHaveFullFinalExamSchedule);
    }

    @Override
    public DataWithCounterDto<FinalExamResponse> getFinalExamByCourseAndSemester(Long semesterId, Long courseId,
                                                                                 Map<String, String> params) {
        Specification specification = Specification.where(CourseClassSpecification.semesterIdEqual(semesterId))
                .and(CourseClassSpecification.courseIdEqual(courseId));

        Pageable paging = pagingHelper.getPageable(params);
        Page<CourseClass> page = courseClassRepository.findAll(specification, paging);
        List<CourseClass> data = page.getContent();
        List<FinalExamResponse> finalExamResponses = data.stream()
                .map(courseClass -> {
                    Exam finalExam = courseClass.getExams().stream()
                            .filter(e -> e.getType() == ExamType.FINAL)
                            .findFirst().orElseGet(() -> Exam.builder().build());;
                    return examMapper.toFinalExamResponse(courseClass, finalExam);
                })
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(finalExamResponses, total);
    }

    @Override
    public AvailableTimeForFinalExamResponse getAvailableTimeFinalExam(Long courseClassId, Integer page) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));
        Integer durationFinalExam = courseClass.getDurationFinalExam();
//        LocalDate freeDateStart = LocalDate.of(2021, 11, 10);

        int daysFromNow = 0;
        if (page != null && page > 0)
            daysFromNow = page * 10;

        LocalDate endStudyDate = getEndStudyTimeDate(courseClassId)
                .orElseGet(LocalDate::now);
        LocalDate freeDateStart = endStudyDate.plusDays(daysFromNow);
        LocalDate freeDateEnd = freeDateStart.plusDays(10);
        DateRange freeDateRange = new DateRange(freeDateStart, freeDateEnd);

        List<DateTimeRange> busyTimeRanges = getBusyTimeRanges(courseClass);
        List<DateTimeRange> freeTimeRanges = scheduleStudyService.getFreeTimeRanges(busyTimeRanges, freeDateRange);
        List<LocalDateTime> startTimeSlots = calculateTimeSlot(freeTimeRanges, durationFinalExam);
        return examMapper.toAvailableTimeForFinalExamResponse(startTimeSlots);
    }

    private Optional<LocalDate> getEndStudyTimeDate(Long courseClassId) {
        List<ScheduleStudy> scheduleStudies =  scheduleStudyRepository.findByCourseClassId(courseClassId);
        return scheduleStudies.stream()
                .map(ss -> {
                    Integer weekLength = ss.getWeekLength();
                    LocalDate startDate = ss.getStartDate();
                    return startDate.plusWeeks(weekLength);
                })
                .max(Comparator.naturalOrder());
    }

    private List<DateTimeRange> getBusyTimeRanges(CourseClass courseClass) {
        List<Long> courseClassIds = getAllCourseClassIdStudentStudyIn(courseClass);
        List<ScheduleStudy> scheduleStudies = scheduleStudyRepository.findAllByCourseClassIdIn(courseClassIds);

        List<Long> courseClassIdsWithoutCurrent = courseClassIds.stream()
                .filter(id -> !id.equals(courseClass.getId()))
                .toList();
        List<Exam> exams = examRepository.findByCourseClassIdInAndType(courseClassIdsWithoutCurrent, ExamType.FINAL);

        return scheduleStudyService.convertToDateTimeRanges(scheduleStudies, exams);
    }

    private List<Long> getAllCourseClassIdStudentStudyIn(CourseClass courseClass) {
        Long semesterId = courseClass.getSemester().getId();
        List<Long> studentIds = courseClass.getStudies().stream()
                .map(study -> study.getStudent().getId())
                .toList();

        Specification<CourseClass> specification = Specification.where(CourseClassSpecification.semesterIdEqual(semesterId))
                .and(CourseClassSpecification.haveAnyStudentStudyIn(studentIds));
        List<CourseClass> courseClasses = courseClassRepository.findAll(specification);
        return courseClasses.stream()
                .map(CourseClass::getId)
                .toList();
    }

    private List<LocalDateTime> calculateTimeSlot(List<DateTimeRange> freeTimeRanges, Integer durationToUse) {
        List<LocalDateTime> timeSlots = new ArrayList<>();
        for (DateTimeRange freeTimeRange : freeTimeRanges) {
            LocalDateTime startTime = freeTimeRange.getStart();
            LocalDateTime endTime = freeTimeRange.getEnd();

            LocalDateTime startTimeRounded = roundToNext30Minutes(startTime);
            boolean isEndTimeAfterUseBeforeEnd;

            do {
                LocalDateTime endTimeAfterUse = startTimeRounded.plusMinutes(durationToUse);
                isEndTimeAfterUseBeforeEnd = endTimeAfterUse.isBefore(endTime) || endTimeAfterUse.isEqual(endTime);
                if (isEndTimeAfterUseBeforeEnd)
                    timeSlots.add(startTimeRounded);

                startTimeRounded = startTimeRounded.plusMinutes(30);
            } while(isEndTimeAfterUseBeforeEnd);
        }

        return timeSlots;
    }

    private LocalDateTime roundToNext30Minutes(LocalDateTime time) {
        int minutes = time.getMinute();
        boolean isRoundUp = minutes % 30 == 0;
        int plusMinutes = isRoundUp ? 0 : 30 - minutes % 30;
        return time.plusMinutes(plusMinutes);
    }

    @Override
    public List<ClassroomResponse> getAvailableRoomForFinalExam(Long courseClassId, GetRoomAvailableFinalExamRequest request) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));

        Integer durationFinalExam = courseClass.getDurationFinalExam();
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(durationFinalExam);
        DateTimeRange timeToUseClassroom = new DateTimeRange(startTime, endTime);

        List<Classroom> classrooms = classroomService.getUnUsedClassrooms(List.of(timeToUseClassroom));
        return classrooms.stream()
                .map(classroomMapper::toClassroomResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateFinalExam(Long courseClassId, UpdateFinalExamRequest request) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));
        Integer durationFinalExam = courseClass.getDurationFinalExam();
        LocalDateTime startTime = request.getTime();
        LocalDateTime endTime = startTime.plusMinutes(durationFinalExam);
        DateTimeRange toUse = DateTimeRange.builder()
                .start(startTime)
                .end(endTime)
                .build();
        validateFinalExamTimeNotBusy(courseClassId, toUse);

        Long classroomId = request.getRoomId();
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASSROOM_NOT_FOUND));

        Exam finalExam = courseClass.getExams().stream()
                .filter(e -> e.getType() == ExamType.FINAL)
                .findFirst().orElseGet(() -> Exam.builder()
                        .type(ExamType.FINAL)
                        .courseClass(courseClass)
                        .build());

        finalExam.setStartTime(startTime);
        finalExam.setClassroom(classroom);
        examRepository.save(finalExam);
    }

    private void validateFinalExamTimeNotBusy(Long courseClassId, DateTimeRange toUse) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));
        List<DateTimeRange> busyTimeRanges = getBusyTimeRanges(courseClass);

        scheduleStudyService.validateTimeRangeIsNotBusy(busyTimeRanges, toUse);
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
            throw new AppException(ErrorCode.COURSE_CLASS_LOCKED);

        Boolean isTeacherTeaching = courseClassRepository.existsByIdAndTeacherId(courseClassId, teacherId);
        if (!isTeacherTeaching)
            throw new AppException(ErrorCode.NOT_TEACH_COURSE_CLASS);
    }
}
