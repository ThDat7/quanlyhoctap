package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.ScheduleStudyClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.dto.request.ScheduleStudyRequest;
import com.thanhdat.quanlyhoctap.dto.response.ClassroomResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseClassScheduleResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseClassWithStatusResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudentClassWithStatusResponse;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.ClassroomMapper;
import com.thanhdat.quanlyhoctap.mapper.CourseClassMapper;
import com.thanhdat.quanlyhoctap.mapper.StudentClassMapper;
import com.thanhdat.quanlyhoctap.repository.ClassroomRepository;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.ScheduleStudyRepository;
import com.thanhdat.quanlyhoctap.service.*;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableServiceImpl implements TimeTableService {
    CourseClassRepository courseClassRepository;
    ScheduleStudyRepository scheduleStudyRepository;
    ClassroomRepository classroomRepository;

    StudentService studentService;
    TeacherService teacherService;
    ScheduleStudyService scheduleStudyService;
    ClassroomService classroomService;

    CourseClassMapper courseClassMapper;
    StudentClassMapper studentClassMapper;
    ClassroomMapper classroomMapper;


    @Override
    public List<CourseClassScheduleResponse> getByCurrentStudentAndSemester(Long semesterId) {
        Long currentStudentId = studentService.getCurrentStudentId();
        return courseClassRepository.findBySemesterIdAndStudentId(semesterId, currentStudentId).stream()
                .map(courseClassMapper::toCourseClassScheduleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseClassScheduleResponse> getByCurrentTeacherAndSemester(Long semesterId) {
        Long currentStudentId = teacherService.getCurrentTeacherId();
        return courseClassRepository.findBySemesterIdAndTeacherId(semesterId, currentStudentId).stream()
                .map(courseClassMapper::toCourseClassScheduleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentClassWithStatusResponse> getStudentClassesWithStatus(Long semesterId) {
        List<Object[]> studentClassesWithStatus = courseClassRepository
                .findStudentClassWithStatusScheduleStudy(semesterId);
        return studentClassesWithStatus.stream()
                .map(this::queryResultToStudentClassWithStatusResponse)
                .collect(Collectors.toList());
    }

    private StudentClassWithStatusResponse queryResultToStudentClassWithStatusResponse(Object[] object) {
        StudentClass studentClass = (StudentClass) object[0];
        boolean status = (boolean) object[1];
        return studentClassMapper.toStudentClassWithStatusResponse(studentClass, status);
    }

    @Override
    public List<CourseClassWithStatusResponse> getCourseClassWithStatus(Long semesterId, Long studentClassId) {
        List<Object[]> courseClassWithStatus = courseClassRepository
                .findCourseClassWithStatusScheduleStudy(semesterId, studentClassId);
        return courseClassWithStatus.stream()
                .map(this::queryResultToCourseClassWithStatusResponse)
                .toList();
    }

    @Override
    public List<CourseClassScheduleResponse> getBySemesterAndStudentClass(Long semesterId, Long studentClassId) {
        return courseClassRepository.findBySemesterIdAndStudentClassId(semesterId, studentClassId).stream()
                .map(courseClassMapper::toCourseClassScheduleResponse)
                .toList();
    }

    @Override
    @Transactional
    public void create(Long courseClassId, ScheduleStudyRequest request) {
        Long classroomId = request.getRoomId();
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASSROOM_NOT_FOUND));
        RoomType roomType = classroom.getRoomType();

        CourseClass courseClass = courseClassRepository.findById(courseClassId )
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));
        List<ScheduleStudy> schedulesStudies = courseClass.getScheduleStudies().stream()
                .filter(ss -> ss.getClassroom().getRoomType() == roomType)
                .collect(Collectors.toList());

        Long idToSave = request.getId();
        ScheduleStudy toSave = schedulesStudies.stream()
                .filter(ss -> ss.getId().equals(idToSave))
                .findFirst()
                .orElseGet(() -> {
                    ScheduleStudy newSS = ScheduleStudy.builder()
                            .courseClass(courseClass)
                            .build();
                    schedulesStudies.add(newSS);
                    return newSS;
                });

        toSave.setStartDate(request.getStartDate());
        toSave.setWeekLength(request.getWeekLength());

        schedulesStudies.forEach(ss -> {
            ss.setShiftLength(request.getShiftLength());
            ss.setShiftStart(request.getShiftStart());
            ss.setClassroom(classroom);
        });

        validateCreateScheduleStudy(courseClassId, request, schedulesStudies);
        scheduleStudyRepository.saveAll(schedulesStudies);
    }


    private void validateCreateScheduleStudy(Long courseClassId,
                                             ScheduleStudyRequest request, List<ScheduleStudy> scheduleStudies) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));
        RoomType roomType = classroomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASSROOM_NOT_FOUND))
                .getRoomType();

        Course course = courseClass.getCourse();
        Integer totalPeriod = roomType == RoomType.CLASS_ROOM ?
                course.getTheoryPeriod() :
                course.getPracticePeriod();
        validatePeriodTime(totalPeriod, scheduleStudies);

        validateNotConflictOtherStudyTime(courseClassId, scheduleStudies);

//        schedule don't overlap?
//        validate start date is in semester ?
    }

    private void validateNotConflictOtherStudyTime(Long courseClassId, List<ScheduleStudy> scheduleStudies) {
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));
        Long semesterId = courseClass.getSemester().getId();
        Long studentClassId = courseClass.getStudentClass().getId();

        List<CourseClass> courseClassOfStudentClass = courseClassRepository
                .findBySemesterIdAndStudentClassId(semesterId, studentClassId);
        List<Long> otherCourseClassId = courseClassOfStudentClass.stream()
                .map(CourseClass::getId)
                .filter(id -> !id.equals(courseClassId))
                .toList();

        List<ScheduleStudy> otherScheduleStudies = scheduleStudyRepository.findAllByCourseClassIdIn(otherCourseClassId);
        List<DateTimeRange> otherTimeUsed = otherScheduleStudies.stream()
                .flatMap(ss -> scheduleStudyService.convertToDateTimeRanges(ss).stream())
                .toList();
        List<DateTimeRange> timeToUse = scheduleStudies.stream()
                .flatMap(ss -> scheduleStudyService.convertToDateTimeRanges(ss).stream())
                .toList();

        timeToUse.forEach(toUse -> scheduleStudyService.validateTimeRangeIsNotBusy(otherTimeUsed, toUse));
    }

    private void validatePeriodTime(Integer totalPeriod, List<ScheduleStudy> scheduleStudies) {
        Integer totalSchedulePeriod = scheduleStudies.stream()
                .reduce(0, (acc, ss) -> acc + ss.getShiftLength() * ss.getWeekLength(), Integer::sum);
        boolean isOver = totalSchedulePeriod > totalPeriod;
        if (isOver)
            throw new AppException(ErrorCode.SCHEDULE_STUDY_TIME_OVER_PERIOD);
    }

    @Override
    public List<ClassroomResponse> getAvailableClassrooms(Long courseClassId,
                                                          ScheduleStudyClassroomAvailableRequest request) {
        ScheduleStudy scheduleStudy = ScheduleStudy.builder()
                .shiftLength(request.getShiftLength())
                .shiftStart(request.getShiftStart())
                .startDate(request.getStartDate())
                .weekLength(request.getWeekLength())
                .build();

        List<DateTimeRange> timeToUse = scheduleStudyService.convertToDateTimeRanges(scheduleStudy);
        RoomType roomType = request.getRoomType();
        List<Classroom> unUsed = classroomService.getUnUsedClassrooms(timeToUse, roomType);
        List<Classroom> currents = classroomRepository.findByCourseClassAndRoomType(courseClassId, roomType);

        Set<Classroom> classrooms = new HashSet<>(unUsed);
        classrooms.addAll(currents);

        return classrooms.stream().map(classroomMapper::toClassroomResponse).toList();
    }

    @Override
    public void delete(Long scheduleStudyId) {
        ScheduleStudy scheduleStudy = scheduleStudyRepository.findById(scheduleStudyId)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_STUDY_NOT_FOUND));
        scheduleStudyRepository.delete(scheduleStudy);
    }

    private CourseClassWithStatusResponse queryResultToCourseClassWithStatusResponse(Object[] object) {
        CourseClass courseClass = (CourseClass) object[0];
        boolean status = (boolean) object[1];
        return courseClassMapper.toCourseClassWithStatusResponse(courseClass, status);
    }
}
