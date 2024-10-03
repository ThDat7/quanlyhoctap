package com.thanhdat.quanlyhoctap.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_NOT_FOUND(1100, "User not found", HttpStatus.NOT_FOUND),
    COURSE_CLASS_NOT_FOUND(1101, "Course Class not found", HttpStatus.NOT_FOUND),
    EDUCATION_PROGRAM_NOT_FOUND(1102, "Education Program not found", HttpStatus.NOT_FOUND),
    COURSE_OUTLINE_NOT_FOUND(1103, "Course Outline not found", HttpStatus.NOT_FOUND),
    COURSE_NOT_FOUND(1104, "Course not found", HttpStatus.NOT_FOUND),
    MAJOR_NOT_FOUND(1105, "Major not found", HttpStatus.NOT_FOUND),
    SEMESTER_NOT_FOUND(1106, "Semester not found", HttpStatus.NOT_FOUND),
    FACULTY_NOT_FOUND(1108, "Faculty not found", HttpStatus.NOT_FOUND),
    NEWS_NOT_FOUND(1109, "News not found", HttpStatus.NOT_FOUND),
    STAFF_NOT_FOUND(1110, "Staff not found", HttpStatus.NOT_FOUND),
    SETTING_NOT_FOUND(1111, "Setting not found", HttpStatus.NOT_FOUND),
    CLASSROOM_NOT_FOUND(1113, "Classroom not found", HttpStatus.NOT_FOUND),
    STUDENT_CLASS_NOT_FOUND(1114, "Student Class not found", HttpStatus.NOT_FOUND),
    TEACHER_NOT_FOUND(1115, "Teacher not found", HttpStatus.NOT_FOUND),
    SCHEDULE_STUDY_NOT_FOUND(1116, "Schedule Study not found", HttpStatus.NOT_FOUND),

    COURSE_OUTLINE_UPLOAD_FAILED(1002, "Course Outline upload failed", HttpStatus.CONFLICT),
    COURSE_OUTLINE_UPDATE_FORBIDDEN(1004, "Don't have permission to edit or Course outline is " +
            "close for editing", HttpStatus.FORBIDDEN),
    REGISTER_COURSE_FAILED(1006, "Register course failed", HttpStatus.CONFLICT),
    UNREGISTER_COURSE_FAILED(1007, "Unregister course failed", HttpStatus.CONFLICT),
    COURSE_ALREADY_REGISTERED(1008, "Course already registered", HttpStatus.CONFLICT),
    COURSE_CLASS_FULL(1009,"Course class is full", HttpStatus.CONFLICT),
    COURSE_NOT_IN_EDUCATION_PROGRAM(1010, "This course is not in your education program", HttpStatus.CONFLICT),
    SEMESTER_FOR_REGISTER_NOT_FOUND(1011, "Semester for register not found", HttpStatus.CONFLICT),
    CLONE_EDUCATION_PROGRAM_FAILED(1012, "Clone education program failed", HttpStatus.CONFLICT),
    EDUCATION_PROGRAM_CLONE_NOT_EMPTY(1013, "Education program of year clone must empty", HttpStatus.CONFLICT),
    MIDTERM_EXAM_START_TIME_NOT_IN_STUDY_TIMES(1014, "Start time of midterm exam must be in the " +
            "schedule study times.", HttpStatus.CONFLICT),
    COURSE_CLASS_LOCKED(1015, "Course class is locked.", HttpStatus.CONFLICT),
    NOT_TEACH_COURSE_CLASS(1016, "You don't teach this course class", HttpStatus.FORBIDDEN),
    DATABASE_DUPLICATE_ENTRY(1017, "Duplicate value {value} in {table}", HttpStatus.CONFLICT),
    TIME_IN_USED(1018, "Time is in used", HttpStatus.CONFLICT),

    SCHEDULE_STUDY_TIME_OVER_PERIOD(1019, "Schedule study time is over period of course", HttpStatus.CONFLICT),

    UNAUTHENTICATED(1500, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1501, "Don't have permission", HttpStatus.FORBIDDEN),
    ;

    private final int code;
    private final String baseMessage;
    private final HttpStatusCode statusCode;
}
