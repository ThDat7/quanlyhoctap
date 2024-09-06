package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegisterOpenResponse {
    private Integer id;
    private String courseCode;
    private String courseName;
//    private String groupName; ???
    private Float courseCredits;
    private String studentClassName;
    private Integer capacity;
    private Integer remaining;
    private String teacherCode;
    private List<ScheduleStudyCourseRegisterResponse> scheduleStudies;
}
