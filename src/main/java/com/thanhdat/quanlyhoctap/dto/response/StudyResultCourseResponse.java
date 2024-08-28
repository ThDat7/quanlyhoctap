package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyResultCourseResponse {
    private String courseCode;
    private String courseName;
    private Float credits;
    private String studentClassName;
    private Float midTermScore;
    private Float finalTermScore;
    private Float totalScore10;
    private Float totalScore4;
    private String totalScoreLetter;
    private Boolean isPassed;
}
