package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.CourseRegisterOpenResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseRegisteredResponse;
import com.thanhdat.quanlyhoctap.dto.response.ScheduleStudyCourseRegisterResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudentCourseRegisterResponse;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.repository.SemesterRepository;
import com.thanhdat.quanlyhoctap.repository.StudyRepository;
import com.thanhdat.quanlyhoctap.service.CourseRegisterService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseRegisterServiceImpl implements CourseRegisterService {
    CourseClassRepository courseClassRepository;
    SemesterRepository semesterRepository;
    StudyRepository studyRepository;
    StudentService studentService;

    @Override
    public void registerCourse(Long courseClassId) {
        Student currentStudent = studentService.getCurrentStudent();
        CourseClass courseClass = courseClassRepository.findById(courseClassId).get();

        validateStudentCanRegister(currentStudent, courseClass);

        LocalDateTime now = LocalDateTime.now();

        Study study = Study.builder()
                .student(currentStudent)
                .courseClass(courseClass)
                .timeRegistered(now)
                .build();
        studyRepository.save(study);
    }

    @Transactional
    public void unregisterCourse(Long courseClassId) {
        Long currentStudentId = studentService.getCurrentStudentId();
        validateIsOnRegisterTime();

        studyRepository.deleteByStudentIdAndCourseClassId(currentStudentId, courseClassId);
    }

    @Override
    public StudentCourseRegisterResponse getStudentCourseRegisterInfo() {
        validateStudentViewRegister();

        Semester semesterRegister = semesterRepository.findForRegister();
        Long currentStudentId = studentService.getCurrentStudentId();
        List<Study> registered = studyRepository.findByStudentIdAndSemesterId(currentStudentId, semesterRegister.getId());
        Map<CourseClass, Integer> openWithRemaining = findOpenRegisterInStudentEducationProgram(currentStudentId);
        return convertToStudentCourseRegisterResponse(semesterRegister, openWithRemaining, registered);
    }

    private Map<CourseClass, Integer> findOpenRegisterInStudentEducationProgram(Long studentId) {
        List<Object[]> results = courseClassRepository.findOpenRegisterInStudentEducationProgram(studentId);
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (CourseClass) result[0],
                        result -> ((Long) result[1]).intValue()
                ));
    }

    private List<ScheduleStudyCourseRegisterResponse> convertToListScheduleStudyCourseRegisterResponse(Set<ScheduleStudy> scheduleStudies) {
        return scheduleStudies.stream()
                .map(scheduleStudy -> ScheduleStudyCourseRegisterResponse.builder()
                        .startDate(scheduleStudy.getStartDate())
                        .weekLength(scheduleStudy.getWeekLength())
                        .roomName(scheduleStudy.getClassroom().getName())
                        .shiftStart(scheduleStudy.getShiftStart())
                        .shiftLength(scheduleStudy.getShiftLength())
                        .build())
                .collect(Collectors.toList());
    }

    private StudentCourseRegisterResponse convertToStudentCourseRegisterResponse(Semester semesterRegister,
                                                                                 Map<CourseClass, Integer> openWithRemaining,
                                                                                 List<Study> registered) {
        String year = String.format("%s - %s", semesterRegister.getYear(), semesterRegister.getYear() + 1);

        List<CourseRegisterOpenResponse> openCourses = openWithRemaining.entrySet().stream()
                .map(entry -> {
                    CourseClass courseClass = entry.getKey();
                    List<ScheduleStudyCourseRegisterResponse> scheduleStudies = convertToListScheduleStudyCourseRegisterResponse(courseClass.getScheduleStudies());
                    Integer remainingSlot = entry.getValue();

                    return CourseRegisterOpenResponse.builder()
                            .id(courseClass.getId())
                            .courseCode(courseClass.getCourse().getCode())
                            .courseName(courseClass.getCourse().getName())
                            .courseCredits(courseClass.getCourse().getCredits())
                            .studentClassName(courseClass.getStudentClass().getName())
                            .capacity(courseClass.getCapacity())
                            .remaining(remainingSlot)
                            .teacherCode(courseClass.getTeacher().getTeacherCode())
                            .scheduleStudies(scheduleStudies)
                            .build();
                })
                .collect(Collectors.toList());

        List<CourseRegisteredResponse> registeredCourse = registered.stream()
                .map(study -> {
                    List<ScheduleStudyCourseRegisterResponse> scheduleStudies = convertToListScheduleStudyCourseRegisterResponse(
                            study.getCourseClass().getScheduleStudies());
                    CourseClass courseClass = study.getCourseClass();
                    return CourseRegisteredResponse.builder()
                            .id(courseClass.getId())
                            .courseCode(courseClass.getCourse().getCode())
                            .courseName(courseClass.getCourse().getName())
                            .courseCredits(courseClass.getCourse().getCredits())
                            .studentClassName(courseClass.getStudentClass().getName())
                            .teacherCode(courseClass.getTeacher().getTeacherCode())
                            .scheduleStudies(scheduleStudies)
                            .timeRegistered(study.getTimeRegistered())
                            .build();
                })
                .collect(Collectors.toList());

        return StudentCourseRegisterResponse.builder()
                .semester(semesterRegister.getSemester())
                .year(year)
                .openCourses(openCourses)
                .registeredCourses(registeredCourse)
                .build();
    }

    private void validateStudentViewRegister() {
//        Logic to check is available current student to view register. Will code later
        Boolean isValidate = true;
        if (!isValidate)
            throw new RuntimeException("You is not available to view register now");
    }

    private void validateStudentCanRegister(Student student, CourseClass courseClass) {
        validateIsOnRegisterTime();
        Long studentId = student.getId();
        Long courseClassId = courseClass.getId();

        Boolean isRegistered = studyRepository.existsByStudentIdAndCourseClassId(studentId, courseClassId);
        if (isRegistered)
            throw new RuntimeException("You have already registered this course");

        Boolean isHaveCourseClassAndNotFull = courseClassRepository.existsByIdAndNotFull(courseClassId);
        if (!isHaveCourseClassAndNotFull)
            throw new RuntimeException("Course class is full");

        Boolean isCourseInStudentEducationProgram = courseClassRepository.isCourseClassInStudentEducationProgram(courseClassId, studentId);
        if (!isCourseInStudentEducationProgram)
            throw new RuntimeException("This course is not in your education program");
    }

    private void validateIsOnRegisterTime() {
//        Logic to check is available current student to register. Will code later
        Boolean isValidate = true;
        if (!isValidate)
            throw new RuntimeException("It's not time to registering for courses now");
    }
}
