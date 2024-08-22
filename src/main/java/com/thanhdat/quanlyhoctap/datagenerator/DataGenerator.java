package com.thanhdat.quanlyhoctap.datagenerator;

import com.github.javafaker.Faker;
import com.thanhdat.quanlyhoctap.datagenerator.helper.GenerateCourseClassHelper;
import com.thanhdat.quanlyhoctap.datagenerator.model.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@AllArgsConstructor
public class DataGenerator {
    private DataLoader dataLoader;
    private SimpleDateFormat simpleDateFormat;
    private final Faker faker = new Faker();
    private GenerateCourseClassHelper generateCourseClassHelper;

    private static final String COMMON_PASSWORD = "123";
    public static final Integer SEMESTER_IN_YEAR = 3;
    private static final Integer DURATION_WEEKS = 15;
    private FacultyRepository facultyRepository;
    private CourseRepository courseRepository;
    private EducationProgramRepository educationProgramRepository;
    private MajorRepository majorRepository;
    private StudentClassRepository studentClassRepository;
    private SemesterRepository semesterRepository;
    private ClassroomRepository classroomRepository;
    private CourseOutlineRepository courseOutlineRepository;
    private TeacherRepository teacherRepository;
    private EducationProgramCourseRepository educationProgramCourseRepository;
    public void generateData() throws Exception {
        createFaculties();
        createCourses();
        createEducationPrograms();
        createStudentClassesWithStudents();
        createSemesters();
        createClassroom();
        generateCourseClassHelper.createCourseClassesForITMajor();
        createCourseOutline();
        generateCourseClassHelper.createStudiesAndScore();
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

            fModel.getMajors().forEach(mName -> {
                Major major = Major.builder()
                        .name(mName)
                        .faculty(faculty)
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
                EducationProgramCourse educationProgramCourse = EducationProgramCourse.builder()
                        .semester(epcModel.getSemester())
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
            students.add(student);

            studentClass.getStudents().add(student);
        }
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
                        "12/06/2024");
        int baseYear = 2021;
        for (int i = 0; i < startDateStrings.size(); i++) {
            Date startDate = simpleDateFormat.parse(startDateStrings.get(i));
            int year = baseYear + i / SEMESTER_IN_YEAR;

            Semester semester = Semester.builder()
                    .year(year)
                    .semester(i % SEMESTER_IN_YEAR + 1)
                    .startDate(startDate)
                    .durationWeeks(DURATION_WEEKS)
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
                CourseOutline courseOutline = CourseOutline.builder()
                        .url(url)
                        .yearPublished(defaultYearPublished)
                        .course(course)
                        .teacher(randomTeacher)
                        .status(defaultOutlineStatus)
                        .deadlineDate(faker.date().past(365 * 10, TimeUnit.DAYS))
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
}
