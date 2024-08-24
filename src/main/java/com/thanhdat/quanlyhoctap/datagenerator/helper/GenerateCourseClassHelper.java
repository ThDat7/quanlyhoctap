package com.thanhdat.quanlyhoctap.datagenerator.helper;

import com.github.javafaker.Faker;
import com.thanhdat.quanlyhoctap.dto.request.ClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.dto.request.TimeToUseClassroomRequest;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.*;
import com.thanhdat.quanlyhoctap.service.ClassroomService;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GenerateCourseClassHelper {

    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private SemesterRepository semesterRepository;
    private EducationProgramRepository educationProgramRepository;
    private ScheduleStudyRepository scheduleStudyRepository;
    private StudentClassRepository studentClassRepository;
    private CourseClassRepository courseClassRepository;
    private ClassroomService classroomService;
    private ScheduleStudyService scheduleStudyService;
    private StudyRepository studyRepository;
    private final Faker faker = new Faker();

    private static final List<Integer> COMMON_SHIFT_STUDY_START = List.of(1, 7);
    private static final Integer THEORY_SHIFT_LENGTH = 4;
    private static final Integer PRACTICE_SHIFT_LENGTH = 3;
    private static final Integer PRACTICE_SHIFT_START = COMMON_SHIFT_STUDY_START.get(1);
    private static final Integer CURRENT_SEMESTER_ID = 8;

    private enum ScheduleType {
        THEORY, PRACTICE
    }

    private ScheduleStudy createScheduleStudy(CourseClass courseClass, Integer dayInWeek,
                                              Integer shiftStart, ScheduleType scheduleType) {
        Integer shiftLength;
        Integer periodByScheduleType;
        RoomType roomType;
        if (scheduleType == ScheduleType.THEORY) {
            shiftLength = THEORY_SHIFT_LENGTH;
            periodByScheduleType = courseClass.getCourse().getTheoryPeriod();
            roomType = RoomType.CLASS_ROOM;
        } else {
            shiftLength = PRACTICE_SHIFT_LENGTH;
            periodByScheduleType = courseClass.getCourse().getPracticePeriod();
            roomType = RoomType.LAB_ROOM;
        }

        Course course = courseClass.getCourse();
        Integer periodStudyInWeek = course.getSessionInWeek() * shiftLength;
        Integer weekLength = (int) Math.ceil(periodByScheduleType / (float) periodStudyInWeek);

        LocalDate startDate = courseClass.getSemester().getStartDate();
        LocalDate endDate = startDate.plusWeeks(weekLength - 1);
        Integer shiftEnd = shiftStart + shiftLength;

        ScheduleStudy scheduleStudy = ScheduleStudy.builder()
                .dayInWeek(dayInWeek)
                .shiftStart(shiftStart)
                .shiftLength(shiftLength)
                .courseClass(courseClass)
                .startDate(startDate)
                .weekLength(weekLength)
                .classroom(null)
                .build();

        List<DateTimeRange> scheduleDateTimeRanges = scheduleStudyService.convertToDateTimeRanges(scheduleStudy);
        List<TimeToUseClassroomRequest> scheduleDateTimeRangesRequest = scheduleDateTimeRanges.stream()
                .map(dateTimeRange -> TimeToUseClassroomRequest.builder()
                        .startTime(dateTimeRange.getStart())
                        .endTime(dateTimeRange.getEnd())
                        .build())
                .collect(Collectors.toList());

        ClassroomAvailableRequest classroomAvailableRequest = ClassroomAvailableRequest.builder()
                .roomType(roomType)
                .timeToUseClassroomRequests(scheduleDateTimeRangesRequest)
                .build();
        List<Classroom> validClassroom = classroomService
                .getUnUsedClassrooms(classroomAvailableRequest);
        Classroom randomClassroom = validClassroom.get(faker.number().numberBetween(0, validClassroom.size() - 1));
        scheduleStudy.setClassroom(randomClassroom);

        return scheduleStudy;
    }

    private Set<ScheduleStudy> generateScheduleStudy(CourseClass courseClass) {
        Set<ScheduleStudy> scheduleStudies = new HashSet<>();
        Course course = courseClass.getCourse();
        Integer practicePeriod = course.getPracticePeriod();

        courseClass.setScheduleStudies(new HashSet<>());
        List<ScheduleStudy> existScheduleStudy
                = scheduleStudyRepository.findByCourseClassSemesterIdAndCourseClassStudentClassId(courseClass.getSemester().getId(),
                courseClass.getStudentClass().getId());

        List<Integer> available = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7));
        Integer sessionInWeek = course.getSessionInWeek();
        for (int i = 1; i <= sessionInWeek; i++) {

            Boolean isFindValidDate;
            do {
                Integer randomDay = available.get(faker.number().numberBetween(0, available.size() - 1));

                List<Integer> startShifts = existScheduleStudy.stream()
                        .filter(scheduleStudy -> scheduleStudy.getDayInWeek().equals(randomDay))
                        .map(ScheduleStudy::getShiftStart)
                        .collect(Collectors.toList());

                Boolean isHavePractice = practicePeriod > 0;
                Integer shiftNeedInDay = isHavePractice ? 2 : 1;

                Integer shiftFreeRemain = COMMON_SHIFT_STUDY_START.size() - startShifts.size();
                isFindValidDate = shiftFreeRemain >= shiftNeedInDay;
                if (!isFindValidDate) {
                    available.remove(randomDay);
                    continue;
                }

                List<Integer> remainShifts = COMMON_SHIFT_STUDY_START.stream()
                        .filter(shift -> !startShifts.contains(shift))
                        .collect(Collectors.toList());
                Integer theoryShiftStart = shiftFreeRemain > 1 ? COMMON_SHIFT_STUDY_START.get(0)
                        : remainShifts.get(0);

                ScheduleStudy theoryScheduleStudy =
                        createScheduleStudy(courseClass, randomDay,
                                theoryShiftStart, ScheduleType.THEORY);
                scheduleStudies.add(theoryScheduleStudy);

                if (isHavePractice) {
                    ScheduleStudy practiceScheduleStudy =
                            createScheduleStudy(courseClass, randomDay,
                                    PRACTICE_SHIFT_START, ScheduleType.PRACTICE);

                            scheduleStudies.add(practiceScheduleStudy);
                }
            } while (!isFindValidDate);
        }
        return scheduleStudies;
    }

    public void createCourseClassesForITMajor() {
        List<Teacher> teachers = teacherRepository.findAll();
        EducationProgram ITEP = educationProgramRepository.findByMajorName("Information Technology").get();
        List<Semester> semesters = semesterRepository.findAll();
        semesters.stream().forEach(semester -> {
            if (semester.getId() > CURRENT_SEMESTER_ID)
                return;

            if (ITEP.getSchoolYear() > semester.getYear())
                return;

            ITEP.getEducationProgramCourses().stream().forEach(epc -> {
                Boolean isMatchYear = epc.getSemester().getYear() == semester.getYear();
                Boolean isMatchSemester = epc.getSemester().getSemester() == semester.getSemester();
                if (!(isMatchSemester && isMatchYear)) return;

                List<StudentClass> studentClasses = studentClassRepository.findByYearAndMajorName(ITEP.getSchoolYear(), "Information Technology");
                studentClasses.stream().forEach(studentClass -> {
                    Teacher randomTeacher = teachers.get(faker.number().numberBetween(0, teachers.size() - 1));
                    Integer capacityClass = studentRepository.countByStudentClassId(studentClass.getId()).intValue();
                    CourseClass courseClass = CourseClass.builder()
                            .course(epc.getCourse())
                            .courseRule(epc.getCourseOutline().getCourseRule())
                            .semester(semester)
                            .teacher(randomTeacher)
                            .studentClass(studentClass)
                            .capacity(capacityClass)
                            .scheduleStudies(null)
                            .studies(null)
                            .finalExam(null)
                            .build();
                    Set<ScheduleStudy> scheduleStudies = generateScheduleStudy(courseClass);
                    FinalExam finalExam = generateFinalExam(scheduleStudies);
                    courseClass.setScheduleStudies(scheduleStudies);
                    courseClass.setFinalExam(finalExam);
                    courseClassRepository.save(courseClass);
                });
            });
        });
    }

    public Set<Score> generateScore(Study study) {
        Set<Score> scores = new HashSet<>();
        for (FactorScore factorScore : FactorScore.values()) {
            Float score = (float) faker.number().randomDouble(2, 0, 10);

            Score s = Score.builder()
                    .factorScore(factorScore)
                    .score(score)
                    .study(study)
                    .build();
            scores.add(s);
        }

        return scores;
    }

    public void createStudiesAndScore() {
        List<Semester> semesters = semesterRepository.findAll();
        semesters.stream().forEach(semester -> {
            if (semester.getId() > CURRENT_SEMESTER_ID)
                return;

            List<StudentClass> studentClasses = studentClassRepository.findAll();
            studentClasses.stream().forEach(studentClass -> {
                Set<Student> students = studentClass.getStudents();
                students.stream().forEach(student -> {
                    List<CourseClass> courseClasses = courseClassRepository.findBySemesterIdAndStudentClassId(semester.getId(), studentClass.getId());
                    courseClasses.stream().forEach(courseClass -> {
                        LocalTime time = LocalTime.of(7, 0, 0);
                        LocalDateTime timeRegistered = LocalDateTime.of(semester.getStartDate(), time);
                        timeRegistered.minusDays(20);

                        Study study = Study.builder()
                                .student(student)
                                .timeRegistered(timeRegistered)
                                .courseClass(courseClass)
                                .build();
                        Set<Score> scores = generateScore(study);
                        study.setScores(scores);
                        studyRepository.save(study);
                    });
                });
            });
        });
    }

    private FinalExam generateFinalExam(Set<ScheduleStudy> scheduleStudies) {
        ScheduleStudy latestEndSchedule = scheduleStudies.stream()
                .max((a, b) -> {
                    LocalDate aEndDate = a.getStartDate().plusWeeks(a.getWeekLength());
                    LocalDate bEndDate = b.getStartDate().plusWeeks(b.getWeekLength());
                    return aEndDate.compareTo(bEndDate);
                }).get();
        LocalDate endStudyDate = latestEndSchedule.getStartDate().plusWeeks(latestEndSchedule.getWeekLength());

        FinalExam finalExam = null;

        Integer dateAfter = 20;
        LocalTime examStartTime = LocalTime.of(7, 0, 0);
        LocalDateTime examStart = LocalDateTime.of(endStudyDate.plusDays(dateAfter), examStartTime);
        List<Classroom> availableClassroom;
        do {
            LocalDateTime examEnd = examStart.plusHours(1).plusMinutes(30);
            TimeToUseClassroomRequest timeToUseClassroomRequest = TimeToUseClassroomRequest.builder()
                    .startTime(examStart)
                    .endTime(examEnd)
                    .build();
            ClassroomAvailableRequest classroomAvailableRequest = ClassroomAvailableRequest.builder()
                    .roomType(RoomType.CLASS_ROOM)
                    .timeToUseClassroomRequests(List.of(timeToUseClassroomRequest))
                    .build();

            availableClassroom = classroomService.getUnUsedClassrooms(classroomAvailableRequest);
            if (availableClassroom.size() == 0) {
                examStart = examStart.plusDays(1);
                continue;
            }

            Classroom randomClassroom = availableClassroom.get(faker.number().numberBetween(0, availableClassroom.size() - 1));
            finalExam = FinalExam.builder()
                    .classroom(randomClassroom)
                    .startTime(examStart)
                    .endTime(examEnd)
                    .build();
        } while(availableClassroom.size() == 0);
        return finalExam;
    }
}
