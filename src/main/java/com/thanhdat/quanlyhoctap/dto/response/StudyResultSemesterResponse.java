package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudyResultSemesterResponse {
    Integer semester;
    Integer year;
    List<StudyResultCourseResponse> courses;
    Float GPA4Semester;
    Float creditsEarnedSemester;
    Float creditsCumulative;
    Float GPA4Cumulative;
}
