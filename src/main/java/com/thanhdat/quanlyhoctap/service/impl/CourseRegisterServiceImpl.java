package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.StudentCourseRegisterResponse;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.mapper.CourseClassMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseRegisterServiceImpl implements CourseRegisterService {
    CourseClassRepository courseClassRepository;
    SemesterRepository semesterRepository;
    StudyRepository studyRepository;

    StudentService studentService;

    CourseClassMapper courseClassMapper;

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
        return courseClassMapper.toStudentCourseRegisterResponse(semesterRegister, openWithRemaining, registered);
    }

    private Map<CourseClass, Integer> findOpenRegisterInStudentEducationProgram(Long studentId) {
        List<Object[]> results = courseClassRepository.findOpenRegisterInStudentEducationProgram(studentId);
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (CourseClass) result[0],
                        result -> ((Long) result[1]).intValue()
                ));
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
