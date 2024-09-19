package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudyResultCourseResponse {
    String courseCode;
    String courseName;
    Float credits;
    String studentClassName;
    Float midTermScore;
    Float finalTermScore;
    Float totalScore10;
    Float totalScore4;
    String totalScoreLetter;
    Boolean isPassed;
}
