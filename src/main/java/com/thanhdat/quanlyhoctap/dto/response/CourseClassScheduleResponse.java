package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseClassScheduleResponse {
    private Integer id;
    private String courseName;
    private String courseCode;
    private Float courseCredits;
    private String studentClassName;
    private String teacherName;
    List<ScheduleStudyTimeTableResponse> schedules;
}

