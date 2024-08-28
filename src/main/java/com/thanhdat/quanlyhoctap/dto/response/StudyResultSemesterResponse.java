package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyResultSemesterResponse {
    private Integer semester;
    private Integer year;
    private List<StudyResultCourseResponse> courses;
    private Float GPA4Semester;
    private Float creditsEarnedSemester;
    private Float creditsCumulative;
    private Float GPA4Cumulative;
}
