package com.thanhdat.quanlyhoctap.datagenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhdat.quanlyhoctap.datagenerator.model.*;
import com.thanhdat.quanlyhoctap.entity.StudentClass;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class DataLoader {
    private static final String FOLDER_PATH = "fake-data/";
    private static final String FACULTY_FILE_PATH = FOLDER_PATH + "faculties.json";
    private static final String COURSE_FILE_PATH = FOLDER_PATH + "courses.json";
    private static final String EDUCATION_PROGRAM_FILE_PATH = FOLDER_PATH + "education_programs.json";
    private static final String STUDENT_CLASS_FILE_PATH = FOLDER_PATH + "student_classes.json";
    private static final String MAJOR_COURSE_OUTLINE_FILE_PATH = FOLDER_PATH + "major_course_outlines.json";

    public List<FacultyWithTeachersModel> loadFacultiesFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(FACULTY_FILE_PATH), objectMapper.getTypeFactory().constructCollectionType(List.class, FacultyWithTeachersModel.class));
    }

    public List<CourseModel> loadCoursesFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(COURSE_FILE_PATH), objectMapper.getTypeFactory().constructCollectionType(List.class, CourseModel.class));
    }

    public List<EducationProgramModel> loadEducationProgramFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(EDUCATION_PROGRAM_FILE_PATH),
                objectMapper.getTypeFactory().constructCollectionType(List.class, EducationProgramModel.class));
    }

    public List<StudentClassModel> loadStudentClassesFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(STUDENT_CLASS_FILE_PATH),
                objectMapper.getTypeFactory().constructCollectionType(List.class, StudentClassModel.class));
    }

    public List<MajorCourseOutlineModel> loadMajorCourseOutlinesFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(MAJOR_COURSE_OUTLINE_FILE_PATH),
                objectMapper.getTypeFactory().constructCollectionType(List.class, MajorCourseOutlineModel.class));
    }
}
