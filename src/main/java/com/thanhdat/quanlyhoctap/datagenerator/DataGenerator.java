package com.thanhdat.quanlyhoctap.datagenerator;

import com.github.javafaker.Faker;
import com.thanhdat.quanlyhoctap.config.DateTimeFormatters;
import com.thanhdat.quanlyhoctap.datagenerator.helper.GenerateCourseClassHelper;
import com.thanhdat.quanlyhoctap.datagenerator.model.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.helper.settingbag.RegisterCourseSettingType;
import com.thanhdat.quanlyhoctap.helper.settingbag.StudySettingType;
import com.thanhdat.quanlyhoctap.repository.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataGenerator {
    DataLoader dataLoader;
    Faker faker;
    GenerateCourseClassHelper generateCourseClassHelper;

    private static final String COMMON_PASSWORD = "123";
    public static final Integer SEMESTER_IN_YEAR = 3;
    private static final Integer DURATION_WEEKS = 15;
    FacultyRepository facultyRepository;
    CourseRepository courseRepository;
    EducationProgramRepository educationProgramRepository;
    MajorRepository majorRepository;
    StudentClassRepository studentClassRepository;
    SemesterRepository semesterRepository;
    ClassroomRepository classroomRepository;
    CourseOutlineRepository courseOutlineRepository;
    TeacherRepository teacherRepository;
    EducationProgramCourseRepository educationProgramCourseRepository;
    SettingRepository settingRepository;
    StaffRepository staffRepository;
    NewsRepository newsRepository;
    public void generateData() throws Exception {
        createSettings();
        createFaculties();
        createCourses();
        createSemesters();
        createEducationPrograms();
        createStudentClassesWithStudents();
        createClassroom();
        createCourseOutline();
        generateCourseClassHelper.createCourseClassesForITMajor();
        generateCourseClassHelper.createStudiesAndScore();
        generateCourseClassHelper.createInvoice();
        createStaffs();
        createNews();
    }

    private User initUser(String name) {
        name = name.toLowerCase();
        String[] tokens = name.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = StringUtils.capitalize(tokens[i]);
        }
        name = String.join(" ", tokens);
        String firstName = tokens[tokens.length - 1];
        String lastName = name.replace(firstName, "").trim();

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .password(COMMON_PASSWORD)
                .build();
    }


    private void createFaculties() throws IOException {
        List<FacultyWithTeachersModel> faculties = dataLoader.loadFacultiesFromFile();
        AtomicInteger index = new AtomicInteger(0);

        faculties.forEach(fModel -> {
            Faculty faculty = Faculty.builder()
                    .name(fModel.getName())
                    .alias(fModel.getAlias())
                    .teachers(new HashSet<>())
                    .majors(new HashSet<>())
                    .build();

            fModel.getTeachers().forEach(tName -> {
                User user = initUser(tName);
                user.setRole(UserRole.TEACHER);
                user.setUsername("teacher" + index.getAndIncrement());
                Teacher teacher = Teacher.builder()
                        .teacherCode("")
                        .user(user)
                        .faculty(faculty)
                        .build();
                faculty.getTeachers().add(teacher);
            });

            fModel.getMajors().forEach(mModel -> {
                Major major = Major.builder()
                        .name(mModel.getName())
                        .alias(mModel.getAlias())
                        .faculty(faculty)
                        .generalTuition(mModel.getGeneralTuition())
                        .specializeTuition(mModel.getSpecializeTuition())
                        .build();
                faculty.getMajors().add(major);
            });
            facultyRepository.save(faculty);
        });
    }

    private void createCourses() throws IOException {
        List<CourseModel> cModels = dataLoader.loadCoursesFromFile();
        cModels.forEach(cModel -> {
            Course course = Course.builder()
                    .name(cModel.getName())
                    .code(cModel.getCode())
                    .credits(cModel.getCredits())
                    .sessionInWeek(cModel.getSessionInWeek())
                    .theoryPeriod(cModel.getTheoryPeriod())
                    .practicePeriod(cModel.getPracticePeriod())
                    .type(CourseType.valueOf(cModel.getType()))
                    .build();
            courseRepository.save(course);
        });
    }

    private void createEducationPrograms() throws IOException {
        List<EducationProgramModel> eModels = dataLoader.loadEducationProgramFromFile();
        eModels.forEach(epModel -> {
            Major major = majorRepository.findByName(epModel.getMajor()).get();
            EducationProgram educationProgram = EducationProgram.builder()
                    .schoolYear(epModel.getSchoolYear())
                    .major(major)
                    .educationProgramCourses(new ArrayList<>())
                    .build();
            epModel.getEducationProgramCourse().forEach(epcModel -> epcModel.getCoursesCode().forEach(cCode -> {
                Course course = courseRepository.findByCode(cCode).get();
                Semester semester = semesterRepository.findByYearAndSemester(epcModel.getYear(), epcModel.getSemester());
                EducationProgramCourse educationProgramCourse = EducationProgramCourse.builder()
                        .semester(semester)
                        .course(course)
                        .educationProgram(educationProgram)
                        .build();

                educationProgram.getEducationProgramCourses().add(educationProgramCourse);
            }));

            educationProgramRepository.save(educationProgram);
        });
    }

    private void generateStudentForStudentClass(StudentClass studentClass, int number) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            User user = User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .username("student" + i)
                    .password(COMMON_PASSWORD)
                    .role(UserRole.STUDENT)
                    .build();
            Student student = Student.builder()
                    .studentCode("SV" + i)
                    .user(user)
                    .studentClass(studentClass)
                    .build();
            Set<StudentStatus> studentStatuses = generateStudentStatuses(student);
            student.setStudentStatuses(studentStatuses);
            students.add(student);

            studentClass.getStudents().add(student);
        }
    }

    private Set<StudentStatus> generateStudentStatuses(Student student) {
        Set<StudentStatus> studentStatuses = new HashSet<>();
        for (int i = 1; i <= GenerateCourseClassHelper.CURRENT_SEMESTER_ID; i++) {
            Semester semester = semesterRepository.findById(i).get();
            StudentStatus studentStatus = StudentStatus.builder()
                    .semester(semester)
                    .student(student)
                    .isLock(false)
                    .build();
            studentStatuses.add(studentStatus);
        }
        return studentStatuses;
    }

    private void createStudentClassesWithStudents() throws IOException {
        List<StudentClassModel> sModels = dataLoader.loadStudentClassesFromFile();
        sModels.forEach(sModel -> {
            Major major = majorRepository.findByName(sModel.getMajor()).get();
            StudentClass studentClass = StudentClass.builder()
                    .year(sModel.getYear())
                    .classOrder(sModel.getClassOrder())
                    .major(major)
                    .students(new HashSet<>())
                    .build();
            generateStudentForStudentClass(studentClass, sModel.getNumberStudentGenerate());
            studentClassRepository.save(studentClass);
        });
    }

    private void createSemesters() throws ParseException {
        List<String> startDateStrings =
                List.of("04/10/2021",
                        "14/02/2022",
                        "13/06/2022",
                        "03/10/2022",
                        "06/02/2023",
                        "05/06/2023",
                        "16/10/2023",
                        "26/02/2024",
                        "17/06/2024");
        int baseYear = 2021;
        for (int i = 0; i < startDateStrings.size(); i++) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateTimeFormatters.DATE_FORMAT);
            LocalDate startDate = LocalDate.parse(startDateStrings.get(i), dateFormatter);
            int year = baseYear + i / SEMESTER_IN_YEAR;
            LocalDateTime lockTime = startDate.plusWeeks(DURATION_WEEKS).plusWeeks(1).atStartOfDay();

            Semester semester = Semester.builder()
                    .year(year)
                    .semester(i % SEMESTER_IN_YEAR + 1)
                    .startDate(startDate)
                    .durationWeeks(DURATION_WEEKS)
                    .lockTime(lockTime)
                    .build();
            semesterRepository.save(semester);
        }
    }

    private void createClassroom() {
        List<String> rowAliases = List.of("A", "B", "C", "D");
        int numberFloors = 4;
        int numberRoomInFloor = 6;
        List<Integer> labRoomInFloor = List.of(4);

        for (String rowAlias : rowAliases)
        {
            for (int i = 0; i < numberFloors; i++) {
                for (int j = 1; j <= numberRoomInFloor; j++) {
                    Classroom classroom = Classroom.builder()
                            .name(rowAlias + i + j)
                            .isAvailable(true)
                            .roomType(labRoomInFloor.contains(j) ? RoomType.LAB_ROOM : RoomType.CLASS_ROOM)
                            .scheduleStudies(null)
                            .build();
                    classroomRepository.save(classroom);
                }
            }
        }
    }

    private void createCourseOutline() throws IOException {
        List<MajorCourseOutlineModel> majorCourseOutlineModels = dataLoader.loadMajorCourseOutlinesFromFile();

        List<EducationProgram> educationPrograms = educationProgramRepository.findAll();
        List<Teacher> teachers = teacherRepository.findAll();

        educationPrograms.forEach(ep -> {
            MajorCourseOutlineModel majorCourseOutlineModel = majorCourseOutlineModels.stream()
                    .filter(m -> m.getMajorName().equals(ep.getMajor().getName()))
                    .findFirst()
                    .get();

            ep.getEducationProgramCourses().forEach(epc -> {
                Teacher randomTeacher = teachers.get(faker.random().nextInt(teachers.size() - 1));
                Course course = epc.getCourse();
                OutlineStatus defaultOutlineStatus = OutlineStatus.PUBLISHED;
                String url = majorCourseOutlineModel.getOutlines().stream()
                        .filter(c -> c.getCourseCode().equals(course.getCode()))
                        .findFirst()
                        .get()
                        .getUrl();
                int defaultYearPublished = 2015;
                Set<EducationProgramCourse> allEpcAssociateWithCourse =
                        new HashSet<>(educationProgramCourseRepository
                                .findByCourse(course));
                List<Float> randomMidTermFactors = List.of((float) 0.3, (float) 0.4, (float) 0.5);
                List<Float> randomPassScores = List.of((float) 5.0, (float) 4.0);
                Float midTermFactor = randomMidTermFactors
                        .get(faker.random().nextInt(randomMidTermFactors.size() - 1));
                Float finalTermFactor = 1 - midTermFactor;
                Float passScore = randomPassScores.get(faker.random().nextInt(randomPassScores.size() - 1));
                CourseRule courseRule = CourseRule.builder()
                        .midTermFactor(midTermFactor)
                        .finalTermFactor(finalTermFactor)
                        .passScore(passScore)
                        .build();

                LocalDateTime deadline = LocalDateTime.now().withYear(defaultYearPublished - 1);


                CourseOutline courseOutline = CourseOutline.builder()
                        .url(url)
                        .yearPublished(defaultYearPublished)
                        .course(course)
                        .courseRule(courseRule)
                        .teacher(randomTeacher)
                        .status(defaultOutlineStatus)
                        .deadline(deadline)
                        .educationProgramCourses(allEpcAssociateWithCourse)
                        .build();
                courseOutlineRepository.save(courseOutline);

                allEpcAssociateWithCourse.forEach(epcAssociateWithCourse -> {
                    epcAssociateWithCourse.setCourseOutline(courseOutline);
                    educationProgramCourseRepository.save(epcAssociateWithCourse);
                });
            });
        });
    }

    private void createSettings() {
        LocalTime timeStartStudy = LocalTime.of(7, 30);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatters.TIME_FORMAT);
        String timeString = timeStartStudy.format(timeFormatter);
        Integer shiftLengthMinutes = 45;

        List<Setting> settings = new ArrayList<>();
        settings.add(Setting.builder()
                .key(StudySettingType.TIME_START_STUDY.name())
                .value(timeString)
                .build());

        settings.add(Setting.builder()
                .key(StudySettingType.SHIFT_LENGTH_MINUTES.name())
                .value(shiftLengthMinutes.toString())
                .build());

        Integer semesterIdForRegister = 9;
        settings.add(Setting.builder()
                .key(RegisterCourseSettingType.SEMESTER_ID_FOR_REGISTER.name())
                .value(semesterIdForRegister.toString())
                .build());

        settingRepository.saveAll(settings);
    }

    private void createStaffs() {
        List<Staff> staffs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .username("staff" + i)
                    .password(COMMON_PASSWORD)
                    .role(UserRole.STAFF)
                    .build();
            Staff staff = Staff.builder()
                    .code("NV" + i)
                    .user(user)
                    .build();
            staffs.add(staff);
        }
        staffRepository.saveAll(staffs);
    }

    private void createNews() {
        List<Staff> staffs = staffRepository.findAll();
        List<News> news = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Staff randomStaff = staffs.get(faker.random().nextInt(staffs.size() - 1));
            News newsItem = News.builder()
                    .title(faker.lorem().sentence())
                    .content("<p>Thong bao minh hoa</p>")
                    .author(randomStaff)
                    .createdAt(LocalDateTime.now().plusSeconds(i))
                    .isImportant(false)
                    .build();
            news.add(newsItem);
        }

        News importantNews = news.get(faker.random().nextInt(news.size() - 1));
        importantNews.setIsImportant(true);
        newsRepository.saveAll(news);
    }
}
