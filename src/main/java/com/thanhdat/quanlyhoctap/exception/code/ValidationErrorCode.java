package com.thanhdat.quanlyhoctap.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ValidationErrorCode {
    INVALID_KEY(2000, "Uncategorized error"),

    COURSE_NAME_SIZE(2001, "Course name size must be between {min} and {max}"),
    COURSE_NAME_NOT_NULL(2001, "Course name must not be null"),
    COURSE_CODE_NOT_NULL(2002, "Course code must not be null"),
    COURSE_CODE_SIZE(2002, "Course code size must be between {min} and {max}"),
    COURSE_CREDITS_NOT_NULL(2003, "Course credits must not be null"),
    COURSE_CREDITS_MIN_VALUE(2003, "Course credits must be at least {value}"),
    COURSE_TYPE_NOT_NULL(2004, "Course type must not be null"),
    COURSE_SESSION_IN_WEEK_NOT_NULL(2005, "Course session in week must not be null"),
    COURSE_SESSION_IN_WEEK_MIN_VALUE(2005, "Course session in week must be at least {value}"),
    COURSE_THEORY_PERIOD_NOT_NULL(2006, "Course theory period must not be null"),
    COURSE_THEORY_PERIOD_MIN_VALUE(2006, "Course theory period must be at least {value}"),
    COURSE_PRACTICE_PERIOD_NOT_NULL(2007, "Course practice period must not be null"),
    COURSE_PRACTICE_PERIOD_MIN_VALUE(2007, "Course practice period must be at least {value}"),

    COURSE_RULE_NOT_NULL(2008, "Course rule must not be null"),
    YEAR_PUBLISHED_MIN_VALUE(2009, "Year published must be at least {value}"),
    COURSE_NOT_NULL(2010, "Course must not be null"),
    TEACHER_NOT_NULL(2011, "Teacher must not be null"),
    COURSE_STATUS_NOT_NULL(2012, "Course status must not be null"),

    MID_TERM_FACTOR_NOT_NULL(2013, "Mid term factor must not be null"),
    MID_TERM_FACTOR_MIN_VALUE(2013, "Mid term factor must be at least {value}"),
    FINAL_TERM_FACTOR_NOT_NULL(2014, "Final term factor must not be null"),
    FINAL_TERM_FACTOR_MIN_VALUE(2014, "Final term factor must be at least {value}"),
    PASS_SCORE_NOT_NULL(2015, "Pass score must not be null"),
    PASS_SCORE_MIN_VALUE(2015, "Pass score must be at least {value}"),
    PASS_SCORE_MAX_VALUE(2015, "Pass score must be at most {value}"),

    SEMESTER_NOT_NULL(2016, "Semester must not be null"),
    SEMESTER_POSITIVE(2016, "Semester must be positive"),

    EDUCATION_PROGRAM_SCHOOL_YEAR_NOT_NULL(2017, "School year must not be null"),
    EDUCATION_PROGRAM_SCHOOL_YEAR_MIN_VALUE(2017, "School year must be at least {value}"),
    MAJOR_NOT_NULL(2018, "Major must not be null"),

    FACULTY_NOT_NULL(2019, "Faculty must not be null"),
    FACULTY_NAME_NOT_NULL(2019, "Faculty name must not be null"),
    FACULTY_NAME_SIZE(2019, "Faculty name size must be between {min} and {max}"),
    FACULTY_ALIAS_NOT_NULL(2020, "Faculty alias must not be null"),
    FACULTY_ALIAS_SIZE(2020, "Faculty alias size must be between {min} and {max}"),

    MAJOR_NAME_NOT_NULL(2021, "Major name must not be null"),
    MAJOR_NAME_SIZE(2021, "Major name size must be between {min} and {max}"),
    MAJOR_ALIAS_NOT_NULL(2022, "Major alias must not be null"),
    MAJOR_ALIAS_SIZE(2022, "Major alias size must be between {min} and {max}"),
    MAJOR_SPECIALIZE_TUITION_NOT_NULL(2024, "Major specialize tuition must not be null"),
    MAJOR_SPECIALIZE_TUITION_POSITIVE(2024, "Major specialize tuition must be positive"),
    MAJOR_GENERAL_TUITION_NOT_NULL(2025, "Major general tuition must not be null"),
    MAJOR_GENERAL_TUITION_POSITIVE(2025, "Major general tuition must be positive"),

    NEWS_TITLE_NOT_NULL(2026, "News title must not be null"),
    NEWS_TITLE_SIZE(2026, "News title size must be between {min} and {max}"),
    NEWS_CONTENT_NOT_NULL(2027, "News content must not be null"),
    NEWS_CONTENT_SIZE(2027, "News content size must be between {min} and {max}"),
    NEWS_IS_IMPORTANT_NOT_NULL(2028, "News important must not be null"),
    NEWS_AUTHOR_NOT_NULL(2029, "News author must not be null"),

    STUDY_NOT_NULL(2030, "Study must not be null"),
    SCORE_MIN_VALUE(2032, "Score must be at least {value}"),
    SCORE_MAX_VALUE(2032, "Score must be at most {value}"),

    SETTING_KEY_NOT_NULL(2033, "Setting key must not be null"),
    SETTING_KEY_SIZE(2033, "Setting key size must be between {min} and {max}"),
    SETTING_VALUE_NOT_NULL(2034, "Setting value must not be null"),
    SETTING_VALUE_SIZE(2034, "Setting value size must be between {min} and {max}"),

    SUM_MID_FINAL_TERM_FACTOR(2035, "Sum of mid term factor and final term factor must be equal to 1"),

    INTROSPECT_TOKEN_NOT_NULL(2036, "Introspect token must not be null"),
    USERNAME_NOT_NULL(2037, "Username must not be null"),
    PASSWORD_NOT_NULL(2038, "Password must not be null"),

    ROOM_AVAILABLE_START_TIME_NOT_NULL(2039, "Start time must not be null"),

    STUDENT_CLASS_NOT_NULL(2041, "Student class must not be null"),
    COURSE_CLASS_CAPACITY_NOT_NULL(2042, "Course class capacity must not be null"),
    COURSE_CLASS_CAPACITY_POSITIVE(2043, "Course class capacity must be positive"),

    SCHEDULE_STUDY_WEEK_LENGTH_NOT_NULL(2044, "Week length must not be null"),
    SCHEDULE_STUDY_WEEK_LENGTH_MIN_VALUE(2044, "Week length must be at least {value}"),
    SCHEDULE_STUDY_START_DATE_NOT_NULL(2045, "Start date must not be null"),
    SCHEDULE_STUDY_SHIFT_START_NOT_NULL(2046, "Shift start must not be null"),
    SCHEDULE_STUDY_SHIFT_START_MIN_VALUE(2046, "Shift start must be at least {value}"),
    SCHEDULE_STUDY_SHIFT_LENGTH_NOT_NULL(2047, "Shift length must not be null"),
    SCHEDULE_STUDY_SHIFT_LENGTH_MIN_VALUE(2047, "Shift length must be at least {value}"),
    SCHEDULE_STUDY_CLASSROOM_ID_NOT_NULL(2048, "Classroom id must not be null"),
    SCHEDULE_STUDY_ROOM_TYPE_NOT_NULL(2049, "Room type must not be null"),
    ;

    private final int code;
    private final String baseMessage;
    private final HttpStatusCode statusCode = HttpStatus.BAD_REQUEST;
}