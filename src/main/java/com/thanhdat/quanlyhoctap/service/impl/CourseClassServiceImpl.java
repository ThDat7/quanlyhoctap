package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.CourseClassCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.CourseClassMapper;
import com.thanhdat.quanlyhoctap.mapper.UtilMapper;
import com.thanhdat.quanlyhoctap.repository.*;
import com.thanhdat.quanlyhoctap.service.CourseClassService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClassServiceImpl implements CourseClassService {
    CourseClassRepository courseClassRepository;
    TeacherRepository teacherRepository;
    StudentClassRepository studentClassRepository;
    SemesterRepository semesterRepository;
    CourseRepository courseRepository;
    CourseOutlineRepository courseOutlineRepository;
    EducationProgramCourseRepository educationProgramCourseRepository;

    TeacherService teacherService;

    PagingHelper pagingHelper;

    UtilMapper utilMapper;
    CourseClassMapper courseClassMapper;

    @Override
    public List<TeacherCourseClassTeachingResponse> getCurrentTeacherTeaching(Long semesterId) {
        Long currentTeacherId = teacherService.getCurrentTeacherId();

        List<CourseClass> courseClasses = courseClassRepository.findByTeacherIdAndSemesterId(currentTeacherId, semesterId);
        return courseClasses.stream()
                .map(courseClassMapper::toTeacherCourseClassTeachingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DataWithCounterDto<CourseClassResponse> getCourseClassBySemesterAndCourse(Long semesterId, Long courseId, Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<CourseClass> page = courseClassRepository.findBySemesterIdAndCourseId(semesterId, courseId, paging);
        List<CourseClass> data = page.getContent();
        List<CourseClassResponse> courseClassResponses = data.stream()
                .map(courseClassMapper::toCourseClassResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(courseClassResponses, total);
    }

    @Override
    public DataWithCounterDto<CourseWithCourseClassCountResponse> getCourseWithCourseClassCountBySemester(Long semesterId, Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<Object[]> page = educationProgramCourseRepository.findCourseRegistered(semesterId, paging);

        List<CourseWithCourseClassCountResponse> data = page.stream()
                .map(this::queryResultToCourseOutlineReuseResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(data, total);
    }

    @Override
    public CourseClassViewCrudResponse getById(Long id) {
        CourseClass courseClass = courseClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course class not found"));
        return courseClassMapper.toCourseClassViewCrudResponse(courseClass);
    }

    @Override
    public List<SelectOptionResponse> getSelectOptionsAvailableTeacher(Long courseId, Long semesterId) {
//        tam thoi chua co logic lay ra danh sach giao vien phu hop
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(e -> utilMapper.toSelectOptionResponse(e.getId(), e.getFullName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectOptionResponse> getSelectOptionsAvailableStudentClass(Long courseId, Long semesterId) {
        List<StudentClass> studentClasses = studentClassRepository.findByCourseAndSemesterInEducationProgram(courseId,
                semesterId);
        return studentClasses.stream()
                .map(e -> utilMapper.toSelectOptionResponse(e.getId(), e.getName()))
                .toList();
    }

    @Override
    @Transactional
    public void create(CourseClassCrudRequest createRequest) {
        validateCourseClassRelation(createRequest);

        long teacherId = createRequest.getTeacherId();
        long semesterId = createRequest.getSemesterId();
        long courseId = createRequest.getCourseId();
        long studentClassId = createRequest.getStudentClassId();


        CourseOutline courseOutline = courseOutlineRepository.findByStudentClassAndCourse(studentClassId, courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_OUTLINE_NOT_FOUND));
        CourseRule courseRule = courseOutline.getCourseRule();

        Semester semester = Semester.builder().id(semesterId).build();
        Course course = Course.builder().id(courseId).build();
        StudentClass studentClass = StudentClass.builder().id(studentClassId).build();
        Teacher teacher = Teacher.builder().id(teacherId).build();

        CourseClass courseClass = CourseClass.builder()
                .semester(semester)
                .course(course)
                .studentClass(studentClass)
                .teacher(teacher)
                .capacity(createRequest.getCapacity())
                .courseRule(courseRule)
                .build();

        courseClassRepository.save(courseClass);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        courseClassRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Long id, CourseClassCrudRequest updateRequest) {
        validateCourseClassRelation(updateRequest);

        CourseClass courseClass = courseClassRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CLASS_NOT_FOUND));
        Course course = Course.builder().id(updateRequest.getCourseId()).build();
        StudentClass studentClass = StudentClass.builder().id(updateRequest.getStudentClassId()).build();
        Teacher teacher = Teacher.builder().id(updateRequest.getTeacherId()).build();
        Semester semester = Semester.builder().id(updateRequest.getSemesterId()).build();

        courseClass.setCourse(course);
        courseClass.setStudentClass(studentClass);
        courseClass.setTeacher(teacher);
        courseClass.setSemester(semester);
        courseClass.setCapacity(updateRequest.getCapacity());
//        recheck course rule

        courseClassRepository.save(courseClass);
    }

    private void validateCourseClassRelation(CourseClassCrudRequest request) {
        Long teacherId = request.getTeacherId();
        Long semesterId = request.getSemesterId();
        Long courseId = request.getCourseId();
        Long studentClassId = request.getStudentClassId();

        if (!teacherRepository.existsById(teacherId)) {
            throw new AppException(ErrorCode.TEACHER_NOT_FOUND);
        }
        if (!semesterRepository.existsById(semesterId)) {
            throw new AppException(ErrorCode.SEMESTER_NOT_FOUND);
        }
        if (!courseRepository.existsById(courseId)) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (!studentClassRepository.existsById(studentClassId)) {
            throw new AppException(ErrorCode.STUDENT_CLASS_NOT_FOUND);
        }
    }

    private CourseWithCourseClassCountResponse queryResultToCourseOutlineReuseResponse(Object[] i) {
        Long id = (Long) i[0];
        String name = (String) i[1];
        Float credit = (Float) i[2];
        Long needCount = (Long) i[3];
        Long presentCount = (Long) i[4];

        return courseClassMapper.toCourseWithCourseClassCountResponse(id, name, credit, needCount, presentCount);
    }

}
