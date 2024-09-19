package com.thanhdat.quanlyhoctap.datagenerator.helper;

import com.github.javafaker.Faker;
import com.thanhdat.quanlyhoctap.dto.request.ClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.dto.request.TimeToUseClassroomRequest;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.*;
import com.thanhdat.quanlyhoctap.service.ClassroomService;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenerateCourseClassHelper {

    TeacherRepository teacherRepository;
    StudentRepository studentRepository;
    SemesterRepository semesterRepository;
    EducationProgramRepository educationProgramRepository;
    ScheduleStudyRepository scheduleStudyRepository;
    StudentClassRepository studentClassRepository;
    CourseClassRepository courseClassRepository;
    ClassroomService classroomService;
    ScheduleStudyService scheduleStudyService;
    StudyRepository studyRepository;
    InvoiceRepository invoiceRepository;
    Faker faker;

    private static final List<Integer> COMMON_SHIFT_STUDY_START = List.of(1, 7);
    private static final Integer THEORY_SHIFT_LENGTH = 4;
    private static final Integer PRACTICE_SHIFT_LENGTH = 3;
    private static final Integer PRACTICE_SHIFT_START = COMMON_SHIFT_STUDY_START.get(1);
    public static final Long CURRENT_SEMESTER_ID = (long) 8;

    private enum ScheduleType {
        THEORY, PRACTICE
    }

    private ScheduleStudy createScheduleStudy(CourseClass courseClass, DayOfWeek dayOfWeek,
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
        Integer gapFromStartDay = dayOfWeek.getValue() - startDate.getDayOfWeek().getValue();
        startDate = startDate.plusDays(gapFromStartDay);

        ScheduleStudy scheduleStudy = ScheduleStudy.builder()
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

        List<DayOfWeek> available = new ArrayList<>(List.of(DayOfWeek.values()));
        Integer sessionInWeek = course.getSessionInWeek();
        for (int i = 1; i <= sessionInWeek; i++) {

            Boolean isFindValidDate;
            do {
                DayOfWeek randomDay = available.get(faker.number().numberBetween(0, available.size() - 1));

                List<Integer> startShifts = existScheduleStudy.stream()
                        .filter(scheduleStudy -> {
                            DayOfWeek dayInWeekSchedule = scheduleStudy.getStartDate().getDayOfWeek();
                            return dayInWeekSchedule == randomDay;
                        })
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
            Long nextSemesterId = CURRENT_SEMESTER_ID + 1;
            if (semester.getId() > nextSemesterId)
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
                            .exams(null)
                            .build();
                    Set<ScheduleStudy> scheduleStudies = generateScheduleStudy(courseClass);
                    courseClass.setScheduleStudies(scheduleStudies);
                    if (scheduleStudies.size() > 0) {
                        Set<Exam> exams = new HashSet<>();
                        exams.add(generateFinalExam(scheduleStudies));
                        exams.add(generateMidtermExam(courseClass));
                        courseClass.setExams(exams);
                    }

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

    public void createInvoice() {
        List<Student> students = studentRepository.findAll();
        List<Semester> semesters = semesterRepository.findAll();
        List<Invoice> invoices = new ArrayList<>();
        semesters.stream()
                .filter(semester -> semester.getId() <= CURRENT_SEMESTER_ID)
                .forEach(semester -> {
                    students.stream().forEach(student -> {
                        Invoice invoice = Invoice.builder()
                                .student(student)
                                .status(InvoiceStatus.PAID)
                                .paymentTime(LocalDateTime.now())
                                .semester(semester)
                                .build();

                        List<Study> studies = studyRepository.findByStudentIdAndSemesterId(student.getId(), semester.getId());
                        Major major = student.getStudentClass().getMajor();
                        Set invoiceDetails = generateInvoiceDetails(invoice, studies, major);
                        invoice.setInvoiceDetails(invoiceDetails);
                        invoices.add(invoice);
                    });
                });
        invoiceRepository.saveAll(invoices);
    }
    private Set<InvoiceDetail> generateInvoiceDetails(Invoice invoice, List<Study> studies, Major major) {
        return studies.stream().map(study -> {
                    CourseClass courseClass = study.getCourseClass();
                    CourseType courseType = courseClass.getCourse().getType();
                    Integer tuitionPerCredit = courseType == CourseType.SPECIALIZE ? major.getSpecializeTuition()
                            : major.getGeneralTuition();
                    return InvoiceDetail.builder()
                            .invoice(invoice)
                            .courseClass(courseClass)
                            .tuition(tuitionPerCredit)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    private Exam generateFinalExam(Set<ScheduleStudy> scheduleStudies) {
        ScheduleStudy latestEndSchedule = scheduleStudies.stream()
                .max((a, b) -> {
                    LocalDate aEndDate = a.getStartDate().plusWeeks(a.getWeekLength());
                    LocalDate bEndDate = b.getStartDate().plusWeeks(b.getWeekLength());
                    return aEndDate.compareTo(bEndDate);
                }).get();
        LocalDate endStudyDate = latestEndSchedule.getStartDate().plusWeeks(latestEndSchedule.getWeekLength());

        Exam exam = null;

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

            CourseClass courseClass = latestEndSchedule.getCourseClass();
            Classroom randomClassroom = availableClassroom.get(faker.number().numberBetween(0, availableClassroom.size() - 1));
            exam = Exam.builder()
                    .courseClass(courseClass)
                    .classroom(randomClassroom)
                    .startTime(examStart)
                    .endTime(examEnd)
                    .type(ExamType.FINAL)
                    .build();
        } while(availableClassroom.size() == 0);
        return exam;
    }

    private Exam generateMidtermExam(CourseClass courseClass) {
        ScheduleStudy randomScheduleStudy =
                courseClass.getScheduleStudies().stream().findFirst().get();

        if (randomScheduleStudy == null) return null;
        List<DateTimeRange> scheduleDateTimeRanges = scheduleStudyService.convertToDateTimeRanges(randomScheduleStudy);
        DateTimeRange lastDayOfStudy = scheduleDateTimeRanges.get(scheduleDateTimeRanges.size() - 1);
        LocalDateTime startTime = lastDayOfStudy.getStart();
        LocalDateTime endTime = lastDayOfStudy.getEnd();

        return Exam.builder()
                .courseClass(courseClass)
                .startTime(startTime)
                .endTime(endTime)
                .classroom(randomScheduleStudy.getClassroom())
                .type(ExamType.MIDTERM)
                .build();
    }
}
